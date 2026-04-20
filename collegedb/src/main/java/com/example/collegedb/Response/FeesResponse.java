package com.example.collegedb.Response;

public class FeesResponse {
    private final Long feesId;
    @SuppressWarnings("FieldMayBeFinal")
    private Long amount;
    @SuppressWarnings("FieldMayBeFinal")
    private String feesStatus;
    @SuppressWarnings("FieldMayBeFinal")
    private String dueDate;
    private final Long studentId;
    private final String studentName;

    public FeesResponse(Long feesId, Long amount, String feesStatus, String dueDate, Long studentId, String studentName) {
        this.feesId = feesId;
        this.amount = amount;
        this.feesStatus = feesStatus;
        this.dueDate = dueDate;
        this.studentId = studentId;
        this.studentName = studentName;
    }

    public Long getFeesId() {return feesId;}
    public Long getAmount() {return amount;}
    public String getFeesStatus() {return feesStatus;}
    public String getDueDate() {return dueDate;}
    public Long getStudentId() {return studentId;}
    public String getStudentName() {return studentName;}
}
