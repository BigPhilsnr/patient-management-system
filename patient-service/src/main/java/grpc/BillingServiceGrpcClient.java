package grpc;


import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BillingServiceGrpcClient {
    private static final Logger log = LoggerFactory.getLogger(BillingServiceGrpcClient.class);
    private final BillingServiceGrpc.BillingServiceBlockingStub blockingStub;


    public BillingServiceGrpcClient(@Value("${billing.service.address:localhost}") String serverAddress,
                                    @Value("${billing.service.port:9001}") int serverPort
                                    ) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort).usePlaintext().build();
        this.blockingStub = BillingServiceGrpc.newBlockingStub(channel);
        log.info("Creating grpc service client with address {} and port {}", serverAddress, serverPort);
    }

    public BillingResponse createBillingAccount(String patientId, String patientName, String email) {
        log.info("Creating billing account for patient ID: {}", patientId);
        billing.BillingRequest request = billing.BillingRequest.newBuilder()
                .setPatientId(patientId)
                .setName(patientName)
                .setEmail(email)
                .build();
        BillingResponse response = blockingStub.createBillingAccount(request);
        log.info("Created billing account for patient ID: {}", response.toString());
        return response;
    }
}
