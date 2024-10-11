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
    @Autowired
    private ReplyRepository replyRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        replyRepository.deleteAll();
    }

    public void testReadAndWriteReply() {
        // Arrange
        Reply savedReply = new Reply("This is a reply", Date.valueOf("2021-10-11"));
        savedReply = replyRepository.save(savedReply);

        // Act
        Reply readReply = replyRepository.findByReplyID(savedReply.getReplyID());

        // Assert
        assertNotNull(readReply);
        assertEquals(savedReply.getReplyID(), readReply.getReplyID());
        assertEquals(savedReply.getDate(), readReply.getDate());
        assertEquals(savedReply.getText(), readReply.getText());
        
    }   


    
}