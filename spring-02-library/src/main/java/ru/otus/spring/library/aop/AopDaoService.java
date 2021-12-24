package ru.otus.spring.library.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import ru.otus.spring.library.exception.DomainNotFound;
import ru.otus.spring.library.exception.RelationDataException;
import ru.otus.spring.library.exception.UniqDomainException;

@Component
@Aspect
public class AopDaoService {

    @AfterThrowing(value = "within(ru.otus.spring.library..*) && " +
            "@target(ru.otus.spring.library.aop.DaoRepository)",
            throwing = "ex"
    )
    public void checkDuplicateKeyException(JoinPoint joinPoint, DuplicateKeyException ex) {
        throw new UniqDomainException(joinPoint.getArgs()[0], ex);
    }

    @AfterThrowing(value = "within(ru.otus.spring.library..*) && " +
            "@target(ru.otus.spring.library.aop.DaoRepository)",
            throwing = "ex"
    )
    public void checkDataIntegrityViolationException(JoinPoint joinPoint, DataIntegrityViolationException ex) {
        throw new RelationDataException(joinPoint.getArgs()[0], ex);
    }

    @AfterThrowing(value = "within(ru.otus.spring.library..*) && " +
            "@target(ru.otus.spring.library.aop.DaoRepository)",
            throwing = "ex"
    )
    public void checkEmptyResultDataAccessException(JoinPoint joinPoint, EmptyResultDataAccessException ex) {
        throw new DomainNotFound(joinPoint.getArgs()[0], ex);
    }
}


