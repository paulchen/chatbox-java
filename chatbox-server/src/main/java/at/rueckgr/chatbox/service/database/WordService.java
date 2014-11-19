package at.rueckgr.chatbox.service.database;

import at.rueckgr.chatbox.database.model.Shout;
import at.rueckgr.chatbox.database.model.ShoutWords;
import at.rueckgr.chatbox.database.model.Word;
import org.apache.commons.lang3.StringUtils;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
@Transactional
public class WordService {
    private @Inject EntityManager em;

    public void updateWords(Shout shoutEntity) {
        Map<Word, Integer> shoutWords = extractWords(shoutEntity);
        TypedQuery<ShoutWords> query = em.createNamedQuery(ShoutWords.QRY_FIND_BY_SHOUT, ShoutWords.class);
        query.setParameter("shout", shoutEntity);
        List<ShoutWords> currentWords = query.getResultList();

        for(ShoutWords word : currentWords) {
            if(!containsWord(shoutWords, word)) {
                em.remove(word);
            }
        }

        // to avoid unique-constraint violations
        em.flush();

        for(Word word : shoutWords.keySet()) {
            if(!containsWord(currentWords, word, shoutWords.get(word))) {
                ShoutWords shoutWord = new ShoutWords(shoutEntity, word, shoutWords.get(word));
                em.persist(shoutWord);
            }
        }
    }

    private boolean containsWord(List<ShoutWords> currentWords, Word word, Integer count) {
        for (ShoutWords currentWord : currentWords) {
            if(currentWord.getWord().equals(word) && currentWord.getCount() == count) {
                return true;
            }
        }

        return false;
    }

    private boolean containsWord(Map<Word, Integer> shoutWords, ShoutWords word) {
        for (Word word1 : shoutWords.keySet()) {
            if(word1.equals(word.getWord()) && shoutWords.get(word1) == word.getCount()) {
                return true;
            }
        }

        return false;
    }

    private Map<Word, Integer> extractWords(Shout shoutEntity) {
        String message = StringUtils.replaceEach(shoutEntity.getMessage(),
                new String[]{",", ".", "!", "?"}, new String[]{"", "", "", ""});
        String[] words = message.split("[\\s]+");

        Map<String, Integer> wordList = new HashMap<String, Integer>();
        for(String foundWord : words) {
            int count = wordList.containsKey(foundWord) ? wordList.get(foundWord)+1 : 1;
            wordList.put(foundWord, count);
        }

        Map<Word, Integer> ret = new HashMap<Word, Integer>(wordList.size());
        for(Map.Entry<String, Integer> wordEntry : wordList.entrySet()) {

            String key = wordEntry.getKey();
            if(key.length() > 100) { // TODO magic number
                key = key.substring(0, 100);
            }
            Word word = findByWord(key);
            ret.put(word, wordEntry.getValue());
        }

        return ret;
    }

    private Word findByWord(String string) {
        try {
            TypedQuery<Word> query = em.createNamedQuery(Word.FIND_BY_WORD, Word.class);
            query.setParameter("word", string);
            return query.getSingleResult();
        }
        catch (NoResultException e) {
            Word word = new Word(string);
            em.persist(word);
            return word;
        }
    }
}
