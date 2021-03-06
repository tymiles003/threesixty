package za.co.yellowfire.threesixty.domain.rating;

import com.github.markash.ui.security.CurrentUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.yellowfire.threesixty.domain.user.User;

import java.util.Objects;

@Service
public class ObjectiveService implements za.co.yellowfire.threesixty.domain.question.Service<Objective> {
	private ObjectiveRepository objectiveRepository;
    private CurrentUserProvider<User> currentUserProvider;

	@Autowired
	public ObjectiveService(
			final ObjectiveRepository objectiveRepository,
            final CurrentUserProvider<User> currentUserProvider) {
		this.objectiveRepository = objectiveRepository;
        this.currentUserProvider = currentUserProvider;
	}

    public ObjectiveRepository getRepository() {
        return objectiveRepository;
    }
	
	public Objective findById(final String id) {
		return objectiveRepository.findOne(id);
	}
	
	public Objective save(final Objective objective) {
        Objects.requireNonNull(objective, "The objective is required");

		this.currentUserProvider.get().ifPresent(objective::auditChangedBy);
		return objectiveRepository.save(objective);
	}
	
	public void delete(final Objective objective) {
        Objects.requireNonNull(objective, "The objective is required");

        objective.setActive(false);
        this.currentUserProvider.get().ifPresent(objective::auditChangedBy);
		objectiveRepository.save(objective);
	}
}
