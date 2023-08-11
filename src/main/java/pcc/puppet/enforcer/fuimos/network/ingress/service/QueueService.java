package pcc.puppet.enforcer.fuimos.network.ingress.service;

import pcc.puppet.enforcer.fuimos.network.ingress.command.MessageSendCommand;
import pcc.puppet.enforcer.fuimos.network.ingress.domain.OperatorQueue;

public interface QueueService {
  OperatorQueue create(MessageSendCommand message);
}
