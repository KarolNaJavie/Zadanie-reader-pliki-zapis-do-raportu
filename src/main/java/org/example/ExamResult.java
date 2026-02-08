package org.example;

import java.time.LocalDate;

public class ExamResult {
    private Student student;
    private Subject subject;
    private int score;
    private LocalDate date;

    public ExamResult(Student student, Subject subject, int score, LocalDate date) {
        this.student = student;
        this.subject = subject;
        this.score = score;
        this.date = date;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
