package com.example.dissertationproject.objects;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class RepLine {
    private TextView txt;
    private EditText reps;

    private EditText weight;
    private Button remove;

    private LinearLayout horizontalLine;

    public RepLine(TextView txt, EditText reps, Button remove, LinearLayout horizontalLine){
        this.txt = txt;
        this.reps = reps;
        this.remove = remove;
        this.horizontalLine = horizontalLine;
    }

    public TextView getTxt() {
        return txt;
    }

    public void setTxt(TextView txt) {
        this.txt = txt;
    }

    public EditText getReps() {
        return reps;
    }

    public void setReps(EditText reps) {
        this.reps = reps;
    }

    public Button getRemove() {
        return remove;
    }

    public void setRemove(Button remove) {
        this.remove = remove;
    }

    public LinearLayout getHorizontalLine() {
        return horizontalLine;
    }

    public void setHorizontalLine(LinearLayout horizontalLine) {
        this.horizontalLine = horizontalLine;
    }

    public EditText getWeight() {
        return weight;
    }

    public void setWeight(EditText weight) {
        this.weight = weight;
    }
}
