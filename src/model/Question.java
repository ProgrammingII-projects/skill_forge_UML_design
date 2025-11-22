package model;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;
import java.util.UUID;

public class Question {
    private String questionId;
    private String text;
    private List<String> options;
    private String correctAnswer;

    public Question(String text, List<String> options, String correctAnswer) {
        this.questionId = UUID.randomUUID().toString();
        this.text = text;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestionId() { return questionId; }
    public String getText() { return text; }
    public List<String> getOptions() { return options; }
    public String getCorrectAnswer() { return correctAnswer; }

    public JSONObject toJson() {
        JSONObject o = new JSONObject();
        o.put("questionId", questionId);
        o.put("text", text);
        o.put("options", new JSONArray(options));
        o.put("correctAnswer", correctAnswer);
        return o;
    }

    public static Question fromJson(JSONObject o) {
        return new Question(
            o.getString("text"),
            o.getJSONArray("options").toList().stream().map(Object::toString).toList(),
            o.getString("correctAnswer")
        );
    }
}
