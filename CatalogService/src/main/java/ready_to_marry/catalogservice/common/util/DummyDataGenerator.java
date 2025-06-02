package ready_to_marry.catalogservice.common.util;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ready_to_marry.catalogservice.item.dto.request.ItemRegisterRequest;
import ready_to_marry.catalogservice.item.enums.CategoryType;
import ready_to_marry.catalogservice.item.enums.FieldType;
import ready_to_marry.catalogservice.item.service.ItemService;

import java.util.List;
import java.util.Random;

@Service
public class DummyDataGenerator {

    private final ItemService itemService;
    private final List<CategoryType> categories = List.of(CategoryType.CEREMONY, CategoryType.SDM, CategoryType.WEDDING_HALL);
    private final List<FieldType> fields = List.of(FieldType.STUDIO, FieldType.DRESS, FieldType.MAKEUP, FieldType.WEDDING_HALL, FieldType.BOUQUET, FieldType.INVITATION, FieldType.VIDEO);
    private final List<String> regions = List.of("서울", "부산", "대구", "인천");
    private final List<String> tags = List.of("신혼가전", "모던", "가전", "생활");
    private final List<String> styles = List.of("모던", "빈티지");
    private final List<String> names = List.of("깔끔한", "화사한", "드레스", "컴퓨터", "이쁜", "웨딩홀", "청첩장");

    public DummyDataGenerator(ItemService itemService) {
        this.itemService = itemService;
    }

    public void generateAndRegisterDummyItems() {
        Random random = new Random();

        for (int i = 0; i < 100; i++) {
            Long partnerId = (long) (random.nextInt(100) + 1);
            FieldType field = fields.get(random.nextInt(fields.size()));
            CategoryType category = categories.get(random.nextInt(categories.size())); // 필드명과 동일하다면 가능

            String name = "더미" + names.get(random.nextInt(names.size())) + names.get(random.nextInt(names.size())) + (i + 1);
            String region = regions.get(random.nextInt(regions.size()));
            Long price = random.nextLong(1_000_000) + 100_000;

            List<String> itemTags = List.of(tags.get(random.nextInt(tags.size())), "기타");
            List<String> itemStyles = List.of(styles.get(random.nextInt(styles.size())));

            MultipartFile thumbnail = createMockImage("thumb-" + i + ".jpg");
            MultipartFile descriptionImage = createMockImage("desc-" + i + ".jpg");

            ItemRegisterRequest request = ItemRegisterRequest.builder()
                    .category(category)
                    .field(field)
                    .name(name)
                    .region(region)
                    .price(price)
                    .address("서울시 중구 더미로 " + i)
                    .description("더미 설명입니다.")
                    .mealPrice(50000)
                    .capacity(200)
                    .parkingCapacity(50)
                    .duration(1)
                    .tags(itemTags)
                    .styles(itemStyles)
                    .build();

            try {
                itemService.register(partnerId, request, thumbnail, descriptionImage);
            } catch (Exception e) {
                System.err.println("더미 아이템 생성 실패: " + e.getMessage());
            }
        }
    }

    private MultipartFile createMockImage(String filename) {
        byte[] content = new byte[10];
        return new MockMultipartFile(filename, filename, "image/jpeg", content);
    }
}
