package ready_to_marry.catalogservice.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ready_to_marry.catalogservice.common.util.DummyDataGenerator;

@RestController
public class DummyItemController {
    private final DummyDataGenerator dummyDataGenerator;

    public DummyItemController(DummyDataGenerator dummyDataGenerator) {
        this.dummyDataGenerator = dummyDataGenerator;
    }

    @GetMapping("/dummy")
    public String generateDummyData() {
        dummyDataGenerator.generateAndRegisterDummyItems();
        return "더미 데이터 100개가 카프카에 전송되었습니다!";
    }
}
