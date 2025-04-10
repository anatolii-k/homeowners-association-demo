package anatolii.k.hoa.community.resident.application;

import anatolii.k.hoa.common.annotations.UseCase;
import anatolii.k.hoa.common.application.UseCaseProcessor;
import anatolii.k.hoa.common.application.UseCaseResponse;
import anatolii.k.hoa.community.resident.domain.RegisterResidentOperation;
import anatolii.k.hoa.community.resident.domain.ResidentRecord;

@UseCase
public class RegisterResidentUseCase {

    public UseCaseResponse<ResidentRecord> registerResident(ResidentRecord residentRecord){
        return UseCaseProcessor.process( ()->registerResidentOperation.register(residentRecord) );
    }

    public RegisterResidentUseCase(RegisterResidentOperation registerResidentOperation) {
        this.registerResidentOperation = registerResidentOperation;
    }

    private final RegisterResidentOperation registerResidentOperation;
}
