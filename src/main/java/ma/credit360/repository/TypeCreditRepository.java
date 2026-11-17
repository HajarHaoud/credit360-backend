package ma.credit360.repository;

import ma.credit360.entity.TypeCredit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeCreditRepository extends JpaRepository<TypeCredit, String> {
    List<TypeCredit> findAllByActifTrue();
}
