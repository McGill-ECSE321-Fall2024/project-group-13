package group_13.game_store.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import group_13.game_store.model.Reply;


@SpringBootTest
public class ReplyRepositoryTests {
    // loading an instance of the local table containing rows of Reply from the local database
    @Autowired
    private ReplyRepository replyRepository;

    // clearing the Reply table that was loaded in before testing
    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        replyRepository.deleteAll();
    }

    public void testReadAndWriteReply() {
        // Arrange
        Reply savedReply = new Reply("This is a reply", Date.valueOf("2021-10-11"));
        // saving the above Reply instance in the cleared Reply table
        savedReply = replyRepository.save(savedReply);

        // Act
        Reply readReply = replyRepository.findByReplyID(savedReply.getReplyID());

        // Assert
        // ensuring the loaded Reply row instances actually exist in the tables of the local database
        assertNotNull(readReply);
        // verifying if all the fields of Reply instance that was created before saving it into the local database matches the fields of the loaded row instance of Reply from the table
        assertEquals(savedReply.getReplyID(), readReply.getReplyID());
        assertEquals(savedReply.getDate(), readReply.getDate());
        assertEquals(savedReply.getText(), readReply.getText());
        
    }   


    
}