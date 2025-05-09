package ready_to_marry.catalogservice.item.enums;

public enum FieldType {
    WEDDING_HALL("웨딩홀"),
    STUDIO("스튜디오"),
    DRESS("드레스"),
    MAKEUP("메이크업"),
    BOUQUET("부케"),
    PRE_VIDEO("식전영상"),
    INVITATION("청첩장"),
    VIDEO("촬영");

    private final String label;

    FieldType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
