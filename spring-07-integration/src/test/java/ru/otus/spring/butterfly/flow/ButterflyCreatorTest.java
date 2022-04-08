package ru.otus.spring.butterfly.flow;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.integration.test.context.MockIntegrationContext;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.integration.test.mock.MockIntegration;
import ru.otus.spring.butterfly.config.SpringIntegrationConfig;
import ru.otus.spring.butterfly.domain.Butterfly;
import ru.otus.spring.butterfly.domain.Caterpillar;
import ru.otus.spring.butterfly.domain.Cocoon;
import ru.otus.spring.butterfly.service.BirdService;
import ru.otus.spring.butterfly.service.TransformService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringIntegrationTest
@SpringBootTest
@Import({SpringIntegrationConfig.class, ButterflyFlow.class})
class ButterflyCreatorTest {

    private static final String BUTTERFLY_TYPE = "butterfly_type_1";
    private static final Long ENTITY_INDEX = 1L;
    private static final Long ENTITY_SIZE = 5L;

    @MockBean(name = "cocoonToButterflyTransformService")
    private TransformService<Cocoon, Butterfly> cocoonToButterflyTransformService;


    @MockBean(name = "caterpillarToCocoonTransformService")
    private TransformService<Caterpillar, Cocoon> caterpillarToCocoonTransformService;

    @MockBean(name = "angryBirdService")
    private BirdService angryBirdService;

    @MockBean(name = "kindBirdService")
    private BirdService kindBirdService;

    @Autowired
    private MockIntegrationContext mockIntegrationContext;

    @Autowired
    private ButterflyCreator butterflyCreator;

    @Test
    void transformToButterflyProcess() {
        Caterpillar caterpillar = new Caterpillar(ENTITY_INDEX, ENTITY_SIZE);
        Cocoon cocoon = new Cocoon(ENTITY_INDEX, ENTITY_SIZE);
        Butterfly expectedButterfly = new Butterfly(BUTTERFLY_TYPE, ENTITY_INDEX, ENTITY_SIZE, true);

        when(caterpillarToCocoonTransformService.transform(caterpillar)).thenReturn(cocoon);
        when(cocoonToButterflyTransformService.transform(cocoon)).thenReturn(expectedButterfly);
        when(kindBirdService.interact(expectedButterfly)).thenReturn(expectedButterfly);

        Butterfly actualButterfly = butterflyCreator.transformToButterflyProcess(caterpillar);

        assertThat(actualButterfly)
                .isEqualTo(expectedButterfly);

    }
}