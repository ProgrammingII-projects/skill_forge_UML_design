package model;


import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Quiz {
    private String quizId;
    private String title;
    private List<Question> questions;

    public Quiz(String title) {
        this.quizId = UUID.randomUUID().toString();
        this.title = title;
        this.questions = new ArrayList<>();
    }

    public String getQuizId() { return quizId; }
    public String getTitle() { return title; }
    public List<Question> getQuestions() { return questions; }

    public void addQuestion(Question q) {
        questions.add(q);
    }

    public JSONObject toJson() {
        JSONObject o = new JSONObject();
        o.put("quizId", quizId);
        o.put("title", title);

        JSONArray qArr = new JSONArray();
        for (Question q : questions) qArr.put(q);
        o.put("questions", qArr);

        return o;
    }

    public static Quiz fromJson(JSONObject o) {
        Quiz quiz = new Quiz(o.getString("title"));
        quiz.quizId = o.getString("quizId");

        JSONArray qArr = o.getJSONArray("questions");
        for (int i = 0; i < qArr.length(); i++) {
            JSONObject qObj = qArr.getJSONObject(i);
            Question q = Question.fromJson(qObj);
            quiz.addQuestion(q);
        }

        return quiz;
    }
}

