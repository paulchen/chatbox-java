package at.rueckgr.chatbox.wrapper;

import at.rueckgr.chatbox.dto.MessageDTO;
import at.rueckgr.chatbox.dto.OnlineUsersInfo;
import at.rueckgr.chatbox.dto.SmileyDTO;
import at.rueckgr.chatbox.dto.UserCategoryDTO;
import at.rueckgr.chatbox.dto.UserDTO;
import at.rueckgr.chatbox.wrapper.exception.ChatboxWrapperException;
import at.rueckgr.chatbox.wrapper.exception.OtherProblemException;
import at.rueckgr.chatbox.wrapper.exception.PollingException;
import at.rueckgr.chatbox.wrapper.exception.PostException;
import at.rueckgr.chatbox.wrapper.exception.WrongMessageCountException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.text.translate.NumericEntityEscaper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatboxImpl implements Chatbox {
    private static final long serialVersionUID = 3629673172370130749L;

    private static Log log = LogFactory.getLog(ChatboxImpl.class);

    private static final String CURRENT_URL = "http://www.informatik-forum.at/misc.php?show=ccbmessages";
    private static final String ARCHIVE_URL = "http://www.informatik-forum.at/misc.php?do=ccarc&page={0}";
    private static final String POST_URL = "http://www.informatik-forum.at/misc.php";

    private static final String LOGIN_URL = "http://www.informatik-forum.at/login.php?do=login";
    private static final String FAQ_URL = "http://www.informatik-forum.at/faq.php";
    private static final String SMILIES_URL = "http://www.informatik-forum.at/misc.php";

    private static final String USERS_URL = "http://www.informatik-forum.at/misc.php?show=ccbusers";

    private static final String TOKEN_PATTERN = "^.*var SECURITYTOKEN = \"([a-f0-9\\-]+)\";.*$";
    private static final String SMILIES_PATTERN = "<div class=\"smilietext td\">([^<]+)</div>[^<]+"
            + "<div class=\"smilieimage td\"><img src=\"[^\"]*\\/([^\\/\"]+)\"[^>]+></div>[^<]+"
            + "<div class=\"smiliedesc td\">([^<]+)</div>";

    private static final String VISIBLE_USERS_PATTERN = "<a href=\"member.php\\?u=([0-9]+)\" title=\"[^\"]+\">(<B><Font Color=\"([a-z]+)\">)?([^<]+)<";
    private static final String INVISIBLE_USERS_PATTERN = "Invisible";

    private ChatboxSession session;
    private transient CloseableHttpClient client;

    private Map<Integer, UserDTO> users = new HashMap<Integer, UserDTO>();
    private Map<String, UserCategoryDTO> userCategories = new HashMap<String, UserCategoryDTO>();

    private interface ResultChecker extends Serializable {
        boolean isOk(String responseString);
    }

    public ChatboxImpl() {
        super();
    }

    public ChatboxImpl(ChatboxSession session) {
        this();
        setSession(session);
    }

    public void login() throws ChatboxWrapperException {
        log.info("Logging in");
        this.session.logUserdata();

        HttpPost postRequest = new HttpPost(LOGIN_URL);
        List<NameValuePair> postData = new ArrayList<NameValuePair>();
        postData.add(new BasicNameValuePair("vb_login_username", this.session.getUsername()));
        postData.add(new BasicNameValuePair("vb_login_password", this.session.getPassword()));
        postData.add(new BasicNameValuePair("vb_login_password_hint", "Password"));
        postData.add(new BasicNameValuePair("cookieuser", "1"));
        postData.add(new BasicNameValuePair("s", ""));
        postData.add(new BasicNameValuePair("securitytoken", "guest"));
        postData.add(new BasicNameValuePair("do", "login"));
        postData.add(new BasicNameValuePair("vb_login_md5password", ""));
        postData.add(new BasicNameValuePair("vb_login_md5password", ""));

        CloseableHttpResponse response = null;
        String responseString;
        try {
            postRequest.setEntity(new UrlEncodedFormEntity(postData));
            response = getClient().execute(postRequest);
            response.close();

            HttpGet getRequest = new HttpGet(FAQ_URL);
            response = getClient().execute(getRequest);
            responseString = getStringFromInputStream(response.getEntity().getContent());
        }
        catch (SocketException e) {
            log.error("Exception during login", e);

            throw new PollingException("Exception during login", e);
        }
        catch (IOException e) {
            log.error("Exception during login", e);

            throw new OtherProblemException("Exception during login", e);
        }
        finally {
            if (response != null) {
                try {
                    response.close();
                }
                catch (IOException e) {
                    log.error("Ignoring subsequent exception", e);
                }
            }

        }

        Pattern pattern = Pattern.compile(TOKEN_PATTERN, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(responseString);
        if (!matcher.matches()) {
            log.info("Login failed");

            throw new OtherProblemException("Unable to login");
        }

        session.setSecurityToken(matcher.group(1));
    }

    private String fetchURL(String url, ResultChecker checker) throws ChatboxWrapperException {
        log.debug("Fetching URL: " + url);

        CloseableHttpResponse response = null;
        try {
            response = getClient().execute(new HttpGet(url));
            String responseString = getStringFromInputStream(response.getEntity().getContent());

            if (!checker.isOk(responseString)) {
                response.close();
                login();

                response = getClient().execute(new HttpGet(url));
                responseString = getStringFromInputStream(response.getEntity().getContent());

                if (!checker.isOk(responseString)) {
                    log.info("Unable to fetch chatbox contents despite re-login");

                    throw new OtherProblemException("Unable to fetch chatbox contents despite re-login");
                }
            }

            return responseString;
        }
        catch (SocketException e) {
            log.error("Exception while fetching chatbox contents", e);

            throw new PollingException("Exception while fetching chatbox contents", e);
        }
        catch (IOException e) {
            log.error("Exception while fetching chatbox contents", e);

            throw new OtherProblemException("Exception while fetching chatbox contents", e);
        }
        finally {
            if (response != null) {
                try {
                    response.close();
                }
                catch (IOException e) {
                    log.error("Ignoring subsequent exception", e);
                }
            }
        }
    }

    private String getStringFromInputStream(InputStream stream) throws IOException {
        StringWriter writer = new StringWriter();
        IOUtils.copy(stream, writer, Charset.forName("ISO-8859-1"));
        return writer.toString();
    }

    public List<MessageDTO> fetchCurrent() throws ChatboxWrapperException {
        log.debug("Fetching chatbox contents");
        this.session.logUserdata();

        final String data = fetchURL(CURRENT_URL, new ResultChecker() {
            private static final long serialVersionUID = -4362478425988622360L;

            public boolean isOk(String responseString) {
                return !responseString.contains("DOCTYPE");
            }
        });

        int lastPos = 0;

        List<MessageDTO> ret = new ArrayList<MessageDTO>();
        DateTimeFormatter dateTimeFormatter = createDateTimeFormatter();
        while (true) {
            int pos1 = data.indexOf("<!-- BEGIN TEMPLATE: vsa_chatbox_bit -->", lastPos);
            if (pos1 == -1) {
                break;
            }
            int pos2 = data.indexOf("<!-- END TEMPLATE: vsa_chatbox_bit -->", pos1);
            if (pos2 == -1) {
                break;
            }
            final String shout = data.substring(pos1, pos2);

            int idpos1 = shout.indexOf("ccbloc=") + 7;
            int idpos2 = shout.indexOf("\"", idpos1);
            int id = Integer.parseInt(shout.substring(idpos1, idpos2));

            int datepos1 = shout.indexOf("[") + 1;
            int datepos2 = shout.indexOf("]");

            LocalDateTime date;
            try {
                date = LocalDateTime.from(dateTimeFormatter.parse(shout.substring(datepos1, datepos2).trim())).plusHours(1);
            }
            catch (DateTimeException e) {
                log.error("Exception while parsing the chatbox content", e);

                throw new OtherProblemException("Exception while parsing the chatbox content", e);
            }

            int memberpos1 = shout.indexOf("member.php?u=") + 13;
            int memberpos2 = shout.indexOf("\"", memberpos1);
            int memberId = Integer.parseInt(shout.substring(memberpos1, memberpos2));

            int memberpos3 = shout.indexOf("</a>", memberpos2);
            String memberNickRaw = shout.substring(memberpos2 + 2, memberpos3);
            String memberNick = memberNickRaw;

            String nickColor = "-";
            int memberpos4 = memberNickRaw.indexOf("<Font Color=");
            if (memberpos4 != -1) {
                int memberpos5 = memberNickRaw.indexOf(">", memberpos4);
                int memberpos6 = memberNickRaw.indexOf("<", memberpos5);
                memberNick = memberNickRaw.substring(memberpos5 + 1, memberpos6);

                int memberpos7 = memberNickRaw.indexOf("\"", memberpos4 + 13);
                nickColor = memberNickRaw.substring(memberpos4 + 13, memberpos7);
            }

            int messagepos1 = shout.indexOf("<td style=\"font-size:;vertical-align:bottom;\">") + 51;
            int messagepos2 = shout.indexOf("\t\t</td>", messagepos1) - 1;
            String message = shout.substring(messagepos1, messagepos2);

            ret.add(createMessageDTO(id, date, memberId, memberNick, nickColor, message.trim()));

            lastPos = pos2;
        }

        // TODO magic number
        checkMessageCount(30, ret.size(), false, CURRENT_URL);

        return ret;
    }

    private void checkMessageCount(int expected, int actual, boolean archive, String url) throws ChatboxWrapperException {
        if (expected != actual) {
            String message = MessageFormat.format("Wrong number of messages fetched from chatbox (expected {0}, actual {1})", expected, actual);

            log.error(message);

            throw new WrongMessageCountException(expected, actual, archive, url);
        }
    }

    private MessageDTO createMessageDTO(int id, LocalDateTime date, int memberId,
                                        String memberNick, String nickColor, String message) {
        if (!this.users.containsKey(memberId)) {
            this.users.put(memberId, createUserDTO(memberId, memberNick, nickColor));
        }

        // TODO magic number
        return new MessageDTO(id, id, 1, message, date, false, this.users.get(memberId));
    }

    private UserDTO createUserDTO(int memberId, String memberNick,
                                  String nickColor) {
        if (!this.userCategories.containsKey(nickColor)) {
            this.userCategories.put(nickColor, new UserCategoryDTO(nickColor, nickColor));
        }

        return new UserDTO(memberId, memberNick, this.userCategories.get(nickColor));
    }

    public List<MessageDTO> fetchArchive(int page) throws ChatboxWrapperException {
        log.debug("Fetching chatbox archive, page " + page);
        this.session.logUserdata();

        String url = MessageFormat.format(ARCHIVE_URL, page);
        final String data = fetchURL(url, new ResultChecker() {
            private static final long serialVersionUID = -6840776392819411021L;

            public boolean isOk(String responseString) {
                return !responseString.isEmpty();
            }
        });

        int lastPos = 0;

        List<MessageDTO> ret = new ArrayList<MessageDTO>();
        DateTimeFormatter dateTimeFormatter = createDateTimeFormatter();
        while (true) {
            int pos1 = data.indexOf("<!-- BEGIN TEMPLATE: vsa_chatbox_archive_bit -->", lastPos);
            if (pos1 == -1) {
                break;
            }
            int pos2 = data.indexOf("<!-- END TEMPLATE: vsa_chatbox_archive_bit -->", pos1);
            if (pos2 == -1) {
                break;
            }
            final String shout = data.substring(pos1, pos2);

            int idpos1 = shout.indexOf("<a name=\"") + 9;
            int idpos2 = shout.indexOf("\"", idpos1);
            int id = Integer.parseInt(shout.substring(idpos1, idpos2));

            int datepos1 = shout.indexOf("  ", idpos2);
            int datepos2 = shout.indexOf("</td>", datepos1);
            LocalDateTime date;
            try {
                date = LocalDateTime.from(dateTimeFormatter.parse(shout.substring(datepos1, datepos2).trim())).plusHours(1);
            }
            catch (DateTimeException e) {
                log.error("Exception while parsing the chatbox content", e);

                throw new OtherProblemException("Exception while parsing the chatbox content", e);
            }

            int memberpos1 = shout.indexOf("member.php?u=") + 13;
            int memberpos2 = shout.indexOf("\"", memberpos1);
            int memberId = Integer.parseInt(shout.substring(memberpos1, memberpos2));

            int memberpos2x = shout.indexOf("class=\"popupctrl\"");
            int memberpos3 = shout.indexOf("</a>", memberpos2x);
            String memberNickRaw = shout.substring(memberpos2x + 18, memberpos3);
            String memberNick = memberNickRaw;

            String nickColor = "-";
            int memberpos4 = memberNickRaw.indexOf("<Font Color=");
            if (memberpos4 != -1) {
                int memberpos5 = memberNickRaw.indexOf(">", memberpos4);
                int memberpos6 = memberNickRaw.indexOf("<", memberpos5);
                memberNick = memberNickRaw.substring(memberpos5 + 1, memberpos6);

                int memberpos7 = memberNickRaw.indexOf("\"", memberpos4 + 13);
                nickColor = memberNickRaw.substring(memberpos4 + 13, memberpos7);
            }

            int messagepos1x = shout.indexOf("vsacb_message");
            int messagepos1 = shout.indexOf(">", messagepos1x) + 10;
            int messagepos2 = shout.indexOf("\t\t\t\t\t</div>", memberpos1) - 2;
            String message = shout.substring(messagepos1, messagepos2);

            ret.add(createMessageDTO(id, date, memberId, memberNick, nickColor, message.trim()));

            lastPos = pos2;
        }

        checkMessageCount(25, ret.size(), true, url);

        return ret;
    }

    private DateTimeFormatter createDateTimeFormatter() {
        // TODO don't always create new DateTimeFormatters
        // TODO timezone fuckup
        return DateTimeFormatter.ofPattern("dd-MM-yy, HH:mm");
    }

    public boolean post(String message) throws Exception {
        log.info("Posting message to chatbox: " + message);
        this.session.logUserdata();

        String result = executePostRequest(message);
        if (!result.isEmpty()) {
            login();

            result = executePostRequest(message);
        }

        if(!result.isEmpty()) {
            throw new PostException("Could not post message to chatbox");
        }

        return result.isEmpty();
    }

    private String executePostRequest(String message) throws Exception {
        HttpPost postRequest = new HttpPost(POST_URL);
        List<NameValuePair> postData = new ArrayList<NameValuePair>();
        postData.add(new BasicNameValuePair("do", "cb_postnew"));
        postData.add(new BasicNameValuePair("vsacb_newmessage", escapeMessageForPosting(message)));
        postData.add(new BasicNameValuePair("securitytoken", this.session.getSecurityToken()));
        postRequest.setEntity(new UrlEncodedFormEntity(postData, Charset.forName("ISO-8859-1")));

        CloseableHttpResponse response = getClient().execute(postRequest);
        String result = getStringFromInputStream(response.getEntity().getContent());
        response.close();

        return result;
    }

    private String escapeMessageForPosting(String message) {
        // TODO improve this
        String output = StringEscapeUtils.ESCAPE_XML.with(NumericEntityEscaper.between(0x7f, Integer.MAX_VALUE)).translate(message);

        output = output.replaceAll("&amp;", "&");
        output = output.replaceAll("&lt;", "<");
        output = output.replaceAll("&gt;", ">");
        output = output.replaceAll("&quot;", "\"");
        output = output.replaceAll("&apos;", "'");

        return output;
    }

    @Override
    public void setSession(ChatboxSession session) {
        this.session = session;
    }

    @Override
    public boolean hasSession() {
        return this.session != null;
    }

    @Override
    public List<SmileyDTO> fetchSmilies() throws ChatboxWrapperException {
        final String data = fetchURL(SMILIES_URL, new ResultChecker() {
            private static final long serialVersionUID = -6840776392819411021L;

            public boolean isOk(String responseString) {
                return !responseString.isEmpty();
            }
        });

        List<SmileyDTO> result = new ArrayList<SmileyDTO>();

        Pattern pattern = Pattern.compile(SMILIES_PATTERN);
        Matcher matcher = pattern.matcher(data);
        while(matcher.find()) {
            String code = matcher.group(1);
            String filename = matcher.group(2);
            String meaning = matcher.group(3);

            SmileyDTO smileyDTO = new SmileyDTO(filename, code, meaning);
            result.add(smileyDTO);
        }

        return result;
    }

    @Override
    public OnlineUsersInfo fetchOnlineUsers() throws ChatboxWrapperException {
        log.debug("Fetching online users");
        this.session.logUserdata();

        final String data = fetchURL(USERS_URL, new ResultChecker() {
            private static final long serialVersionUID = -5443491000158508968L;

            public boolean isOk(String responseString) {
                return !responseString.contains("DOCTYPE");
            }
        });

        Pattern visibleUsersPattern = Pattern.compile(VISIBLE_USERS_PATTERN);
        Matcher visibleUsersMatcher = visibleUsersPattern.matcher(data);
        List<UserDTO> visibleUsers = new ArrayList<UserDTO>();
        while(visibleUsersMatcher.find()) {
            String userIdString = visibleUsersMatcher.group(1);
            String userColor = visibleUsersMatcher.group(3);
            if(userColor == null) {
                userColor = "-";
            }
            String username = visibleUsersMatcher.group(4);

            log.debug(MessageFormat.format("User online: {0} {1} {2}", userIdString, userColor, username));

            visibleUsers.add(createUserDTO(Integer.parseInt(userIdString), username, userColor));
        }

        Pattern invisibleUsersPattern = Pattern.compile(INVISIBLE_USERS_PATTERN);
        Matcher invisibleUsersMatcher = invisibleUsersPattern.matcher(data);
        int invisibleUsers = 0;
        while(invisibleUsersMatcher.find()) {
            invisibleUsers++;
        }

        log.debug(MessageFormat.format("{0} invisible user(s) online", invisibleUsers));
        log.info(MessageFormat.format("{0} user(s) currently online", visibleUsers.size()+invisibleUsers));

        return new OnlineUsersInfo(visibleUsers, invisibleUsers);
    }

    private CloseableHttpClient getClient() {
        if(this.client == null) {
            this.client = HttpClients.custom().setDefaultCookieStore(this.session.getCookieStore()).build();
        }
        return this.client;
    }
}
