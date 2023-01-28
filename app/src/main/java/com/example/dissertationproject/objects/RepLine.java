package com.example.dissertationproject.objects;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class RepLine {
    private TextView txt;
    private EditText reps;
    private Button remove;

    public RepLine(TextView txt, EditText reps, Button remove){
        this.txt = txt;
        this.reps = reps;
        this.remove = remove;
    }
}
