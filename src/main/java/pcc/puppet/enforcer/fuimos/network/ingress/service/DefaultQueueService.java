package pcc.puppet.enforcer.fuimos.network.ingress.service;

import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pcc.puppet.enforcer.fuimos.network.ingress.adapters.repository.OperatorQueueRepository;
import pcc.puppet.enforcer.fuimos.network.ingress.command.MessageSendCommand;
import pcc.puppet.enforcer.fuimos.network.ingress.domain.OperatorQueue;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultQueueService implements
    QueueService {
private final OperatorQueueRepository operatorQueueRepository;
  @Override
  public OperatorQueue create(MessageSendCommand message) {
    OperatorQueue operatorQueue = OperatorQueue.builder()
        .id(UlidCreator.getMonotonicUlid().toLowerCase())
        .operatorId(message.)
        .build();
    return operatorQueueRepository.save(operatorQueue);
  }
}
