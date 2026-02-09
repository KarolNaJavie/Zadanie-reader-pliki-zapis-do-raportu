package org.example;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    static void main() {

//        ==========================
//        ZADANIE: Analizator wyników egzaminów
//                ==========================*
//                Masz plik "exam-results.txt".
//                W pliku są poprawne i celowo błędne linie. Program ma:
//        wczytać poprawne dane do obiektów
//        pominąć błędne linie (bez wywalania programu)
//        zrobić kilka analiz
//        zapisać raport do "report.txt"
//                *
//                --------------------------
//                        FORMAT LINII (poprawnej)
//                --------------------------
//        STUDENT_ID;NAME;SUBJECT;SCORE;EXAM_DATE*
//                SUBJECT ma być enum: MATH, JAVA, ENGLISH
//        SCORE: 0..100
//        EXAM_DATE: yyyy-MM-dd (LocalDate)*
//                --------------------------
//                        WYMAGANIA
//                                --------------------------
//        1) Model danych:
//        enum Subject { MATH, JAVA, ENGLISH }
//        Student (id, name)
//        ExamResult (student, subject, score, date
//                *
//                2) Wczytanie pliku:
//        użyj BufferedReader do czytania "exam-results.txt"
//        każdą linię parsuj w osobnej metodzie np. parseLine(String line)
//        jeśli linia jest błędna -> pomiń i wypisz info: "Pomijam złą linię: ..."
//                *
//                BŁĘDNA LINIA to m.in.:
//        zły format (np. brak 5 pól po split(";"))
//        subject nie jest w enumie (np. HISTORY)
//        score poza 0..100 (np. 120)
//        data nie do sparsowania (np. 2024-99-99)
//        trzymasz wyniki w List<ExamResult>
//                *
//                3) Analizy (po wczytaniu):
//        A) wypisz: ile POPRAWNYCH wyników wczytano
//        B) najlepszy wynik OGÓLNIE:
//        kto (id + imię), z jakiego przedmiotu, ile punktów, data
//                (Stream + max(...) mile widziane)
//        C) średnia dla studenta o ID "S-1":
//        licz jako BigDecimal z 2 miejscami po przecinku
//                (nie int / double)
//        D) wyniki w zakresie dat: 2024-01-15 do 2024-02-01 (włącznie):
//        posortuj po dacie rosnąco
//        wypisz w czytelnej formie
//*
//        4) Zapis raportu:
//        zapisz plik "report.txt" (BufferedWriter)
//                raport ma zawierać:
//        liczbę poprawnych wyników
//        najlepszy wynik ogólnie
//        średnią dla S-1
//        listę wyników z zakresu dat (punkt D)
//                *
//                --------------------------
//                        FORMAT WYPISYWANIA
//                --------------------------
//        S-1 (Jan Kowalski) | JAVA | 74 | 2024-01-20

        List<String> results = new ArrayList<>();
        List<ExamResult> resultsExam = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("exam-results.txt"))) {   //ladowanie resultatow do listy
            String line;

            while ((line = reader.readLine()) != null) {
                results.add(line);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        for (String line : results) {
            ExamResult examResults = checkLine(line);
            if (examResults != null) {
                resultsExam.add(examResults);
            }
        }

//        A) wypisz: ile POPRAWNYCH wyników wczytano
//        B) najlepszy wynik OGÓLNIE:
//        kto (id + imię), z jakiego przedmiotu, ile punktów, data
//                (Stream + max(...) mile widziane)
//        C) średnia dla studenta o ID "S-1":
//        licz jako BigDecimal z 2 miejscami po przecinku
//                (nie int / double)
//        D) wyniki w zakresie dat: 2024-01-15 do 2024-02-01 (włącznie):
//        posortuj po dacie rosnąco
//        wypisz w czytelnej formie
//*
//        4) Zapis raportu:
//        zapisz plik "report.txt" (BufferedWriter)
//                raport ma zawierać:
//        liczbę poprawnych wyników
//        najlepszy wynik ogólnie
//        średnią dla S-1
//        listę wyników z zakresu dat (punkt D)

//        resultsExam = resultsExam.stream().filter(n -> !n.getSubject().equals(Subject.HISTORY)).toList();
        Optional<ExamResult> bestResult = resultsExam.stream().max(Comparator.comparing(ExamResult::getScore));
        List<ExamResult> wynikiStudentaS1 = resultsExam.stream().filter(n -> n.getStudent().getId() == 1).toList();
        double sum = wynikiStudentaS1.stream().mapToDouble(ExamResult::getScore).sum();
        BigDecimal average = BigDecimal.valueOf(sum / wynikiStudentaS1.size());
        List<ExamResult> resultFromPeriod = resultsExam.stream().filter(n -> n.getDate().isBefore(LocalDate.of(2024, 2, 2))
                && n.getDate().isAfter(LocalDate.of(2024, 1, 14))).sorted(Comparator.comparing(ExamResult::getDate)).toList();


        System.out.println("\nWczytano " + resultsExam.size());
        System.out.println("Najlepszy wynik osiagnal " + bestResult.get().getStudent().getName() + ", zdobyl " + bestResult.get().getScore() + " punktow z przedmiotu: " + bestResult.get().getSubject().getDisplayName() + ", data: " + bestResult.get().getDate());
        System.out.println("Srednia ocen studenta o ID- S-1 to: " + average.setScale(2, RoundingMode.HALF_UP));


//        for (ExamResult wynik : resultFromPeriod) {
//            System.out.println(wynik.student.getName() + " ID: " + wynik.student.getId() + ", wynik: " + wynik.score + ", data: " + wynik.date);
//        }

        //zapis do raportu
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("report.txt"))) {
            writer.write("Wczytano " + resultsExam.size());
            writer.newLine();
            writer.write("Najlepszy wynik osiagnal " + bestResult.get().getStudent().getName() + ", zdobyl " + bestResult.get().getScore() + " punktow z przedmiotu: " + bestResult.get().getSubject().getDisplayName() + ", data: " + bestResult.get().getDate());
            writer.newLine();
            writer.write("Srednia ocen studenta o ID- S-1 to: " + average.setScale(2, RoundingMode.HALF_UP));
            writer.newLine();
            System.out.println("\nWyniki miedzy 2024-01-15 a 2024-02-01 :");
            writer.write("\nWyniki miedzy 2024-01-15 a 2024-02-01 :");
            for (ExamResult wynik : resultFromPeriod) {
                System.out.println(wynik.getStudent().getName() + " ID: " + wynik.getStudent().getId() + ", przedmiot: "+ wynik.getSubject().getDisplayName() + ", wynik: " + wynik.getScore() + ", data: " + wynik.getDate());
                writer.write(wynik.getStudent().getName() + " ID: " + wynik.getStudent().getId() +  ", przedmiot: "+ wynik.getSubject().getDisplayName() + ", wynik: " + wynik.getScore() + ", data: " + wynik.getDate());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static ExamResult checkLine(String line) {
        if (line != null) {
            Pattern pattern = Pattern.compile("S-(\\d+);([^;]+);(\\w+);(\\d+);(\\d{4})-(\\d{2})-(\\d{2})");
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()
                    && (Integer.parseInt(matcher.group(7).trim()) < 32)
                    && (Integer.parseInt(matcher.group(6).trim()) < 13)
                    && (Integer.parseInt(matcher.group(4).trim()) <= 100)
                    && (Integer.parseInt(matcher.group(4).trim()) >= 0)
                    && ((matcher.group(3).trim().equalsIgnoreCase("MATH")) || (matcher.group(3).trim().equalsIgnoreCase("JAVA")) || (matcher.group(3).trim().equalsIgnoreCase("ENGLISH")))) {
                Student student = new Student(Integer.parseInt(matcher.group(1).trim()), matcher.group(2).trim());
                LocalDate date = LocalDate.of(Integer.parseInt(matcher.group(5).trim()), Integer.parseInt(matcher.group(6).trim()), Integer.parseInt(matcher.group(7).trim()));
                return new ExamResult(student, Subject.valueOf(matcher.group(3).trim()), Integer.parseInt(matcher.group(4).trim()), date);
            } else {
                System.out.println("...Skipping wrong value in line");
            }
        }
        System.out.println("...Skipping wrong line");
        return null;
    }
}
