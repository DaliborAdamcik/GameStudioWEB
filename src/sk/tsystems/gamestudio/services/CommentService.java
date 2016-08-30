package sk.tsystems.gamestudio.services;
import java.util.List;

import sk.tsystems.gamestudio.entity.*;

public interface CommentService extends AutoCloseable {
	boolean addComment(CommentEntity comment);
	List<CommentEntity> commentsFor(GameEntity game);
	List<CommentEntity> commentsFor(UserEntity user);
	void setLimit(int limit);
}
