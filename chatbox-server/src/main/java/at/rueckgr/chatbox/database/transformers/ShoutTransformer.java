package at.rueckgr.chatbox.database.transformers;

import at.rueckgr.chatbox.database.model.Shout;
import at.rueckgr.chatbox.database.model.ShoutPK;
import at.rueckgr.chatbox.database.model.ShoutSmileys;
import at.rueckgr.chatbox.database.model.ShoutWords;
import at.rueckgr.chatbox.database.model.Smiley;
import at.rueckgr.chatbox.database.model.Word;
import at.rueckgr.chatbox.dto.MessageDTO;
import at.rueckgr.chatbox.unparser.MessageUnparser;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class ShoutTransformer implements Transformer<Shout, MessageDTO>, Serializable {

    @Inject
    private ShoutIdTransformer shoutIdTransformer;

    @Inject
    private UserTransformer userTransformer;

    @Inject
    private MessageUnparser messageUnparser;

    @Inject
    private EntityManager em;

    @Override
    public MessageDTO entityToDTO(Shout shoutEntity) {
        if(shoutEntity == null) {
            return null;
        }

        // TODO use factory
        MessageDTO messageDTO = new MessageDTO();
        updateDTO(messageDTO, shoutEntity);
        return messageDTO;
    }

    @Override
    public Shout dtoToEntity(MessageDTO messageDTO) {
        if(messageDTO == null) {
            return null;
        }

        ShoutPK shoutPK = shoutIdTransformer.dtoToEntity(messageDTO.getMessageId());
        Shout shoutEntity = em.find(Shout.class, shoutPK);
        if(shoutEntity == null) {
            shoutEntity = new Shout();
        }

        updateEntity(shoutEntity, messageDTO);

        return shoutEntity;
    }

    @Override
    public void updateDTO(MessageDTO messageDTO, Shout shoutEntity) {
        String rawMessage = shoutEntity.getMessage();
        String message = messageUnparser.unparse(rawMessage);

        messageDTO.setMessageId(shoutIdTransformer.entityToDTO(shoutEntity.getId()));
        // messageDTO.setDate(shoutEntity.getDate());
        // TODO fix this ugly fuckup
        messageDTO.setDate(new Date(shoutEntity.getDate().getTime()+3600000));
        messageDTO.setDeleted(shoutEntity.getDeleted());
        messageDTO.setRawMessage(rawMessage);
        messageDTO.setMessage(message);
        messageDTO.setUser(userTransformer.entityToDTO(shoutEntity.getUser()));
    }

    @Override
    public void updateEntity(Shout shoutEntity, MessageDTO messageDTO) {
        // TODO create ShoutRevision if anything changes
        shoutEntity.setId(shoutIdTransformer.dtoToEntity(messageDTO.getMessageId()));
        // shoutEntity.setDate(messageDTO.getDate());
        // TODO fix this ugly fuckup
        shoutEntity.setDate(new Date(messageDTO.getDate().getTime()-3600000));
        shoutEntity.setDeleted(messageDTO.isDeleted());
        shoutEntity.setMessage(messageDTO.getRawMessage());
        shoutEntity.setUser(userTransformer.dtoToEntity(messageDTO.getUser()));

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(messageDTO.getDate());
        shoutEntity.setYear(calendar.get(Calendar.YEAR));
        shoutEntity.setMonth(calendar.get(Calendar.MONTH) + 1);
        shoutEntity.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        shoutEntity.setHour(calendar.get(Calendar.HOUR_OF_DAY));

        updateWords(shoutEntity);
        updateSmilies(shoutEntity);
    }

    private void updateSmilies(Shout shoutEntity) {
        List<ShoutSmileys> shoutSmileyList = extractSmileys(shoutEntity);
        shoutEntity.setSmilies(shoutSmileyList);
    }

    private List<ShoutSmileys> extractSmileys(Shout shoutEntity) {
        final String smileyPattern = "\"/?(pics|images)/([no]b/)?smilies/[^\"]*\\.(gif|png|jpg)";

        String message = shoutEntity.getMessage();
        Pattern pattern = Pattern.compile(smileyPattern);
        Matcher matcher = pattern.matcher(message);

        // TODO does this work correctly?
        Map<String, Integer> smileyList = new HashMap<String, Integer>();
        while(matcher.find()) {
            String smileyFilename = matcher.group(0);
            smileyFilename = smileyFilename.substring(smileyFilename.indexOf('/') + 1);
            int count = smileyList.containsKey(smileyFilename) ? smileyList.get(smileyFilename)+1 : 1;
            smileyList.put(smileyFilename, count);
        }

        List<ShoutSmileys> ret = new ArrayList<ShoutSmileys>(smileyList.size());
        for(Map.Entry<String, Integer> smileyEntry : smileyList.entrySet()) {
            Smiley smiley;
            try {
                TypedQuery<Smiley> query = em.createNamedQuery(Smiley.FIND_BY_FILENAME, Smiley.class);
                query.setParameter("filename", smileyEntry.getKey());
                smiley = query.getSingleResult();
            }
            catch (NoResultException e) {
                smiley = new Smiley(smileyEntry.getKey());
                em.persist(smiley);
                em.flush();
            }

            ret.add(new ShoutSmileys(shoutEntity, smiley, smileyEntry.getValue()));
        }

        return ret;
    }

    private void updateWords(Shout shoutEntity) {
        List<ShoutWords> wordList = extractWords(shoutEntity);
        shoutEntity.setWords(wordList);
    }

    private List<ShoutWords> extractWords(Shout shoutEntity) {
        String message = StringUtils.replaceEach(shoutEntity.getMessage(),
                new String[]{",", ".", "!", "?"}, new String[]{"", "", "", ""});
        // TODO doesn't work
        String[] words = message.split("[\\s]+]");

        Map<String, Integer> wordList = new HashMap<String, Integer>();
        for(String foundWord : words) {
            int count = wordList.containsKey(foundWord) ? wordList.get(foundWord)+1 : 1;
            wordList.put(foundWord, count);
        }

        List<ShoutWords> ret = new ArrayList<ShoutWords>(wordList.size());
        for(Map.Entry<String, Integer> wordEntry : wordList.entrySet()) {
            Word word;
            try {
                TypedQuery<Word> query = em.createNamedQuery(Word.FIND_BY_WORD, Word.class);
                query.setParameter("word", wordEntry.getKey());
                word = query.getSingleResult();
            }
            catch (NoResultException e) {
                word = new Word(wordEntry.getKey());
                em.persist(word);
                em.flush();
            }

            ret.add(new ShoutWords(shoutEntity, word, wordEntry.getValue()));
        }

        return ret;
    }
}
