package pcc.puppet.enforcer.fuimos.network.ingress.adapters.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pcc.puppet.enforcer.fuimos.network.ingress.domain.OperatorQueue;

@Repository
public interface OperatorQueueRepository extends CrudRepository<OperatorQueue, String> {

}
