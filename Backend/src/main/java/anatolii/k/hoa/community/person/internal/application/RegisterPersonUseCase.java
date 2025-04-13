package anatolii.k.hoa.community.person.internal.application;

import anatolii.k.hoa.common.annotations.UseCase;
import anatolii.k.hoa.common.application.UseCaseProcessor;
import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.community.person.internal.domain.Person;
import anatolii.k.hoa.community.person.internal.domain.RegisterPersonOperation;
import anatolii.k.hoa.community.person.internal.domain.RegisterPersonRequest;

@UseCase
public class RegisterPersonUseCase {

    public UseCaseResponse<Person> register(RegisterPersonRequest registerPersonRequest) {
        return UseCaseProcessor.process(
                ()-> registerPersonOperation.register(registerPersonRequest)
        );
    }

    public RegisterPersonUseCase(RegisterPersonOperation registerPersonOperation) {
        this.registerPersonOperation = registerPersonOperation;
    }

    private final RegisterPersonOperation registerPersonOperation;
}
