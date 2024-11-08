package group_13.game_store.repository;

import org.springframework.data.repository.CrudRepository;
import group_13.game_store.model.Reply;

public interface ReplyRepository extends CrudRepository<Reply, Integer> {
    // allows instantiation of an Reply instance that is stored in the local database by its unique ID
    public Reply findByReplyID(int replyID);
    
    //Method to find the reply associated to a reviewID
    public Reply findByReview_ReviewID(int reviewID);
}
