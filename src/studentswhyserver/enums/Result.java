package studentswhyserver.enums;

public enum Result {
    SUCCESS("Success"), FAIL("Fail"), ALREADY_DONE("Already-Done"), NO_RIGHTS("No-Rights");


    private Result(String value) {
        this.value = value;
    }

    private String value;

    @Override
    public String toString() {
        return value;
    }
}
