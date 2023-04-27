/**
 * @author Sandesh Adhikari
 */
package com.example.dissertationproject.objects;

import android.content.Context;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dissertationproject.R;
import com.example.dissertationproject.ui.workouts.ActiveWorkoutAdapter;

import org.w3c.dom.Text;

public class RepLine {

    private static final String ENTER_TARGET_REPS = "Enter the target reps";
    private static final String TARGET_REPS = "Target Reps: ";
    private static final String REPS = "Enter the reps: ";
    private static final String WEIGHT = "Weight: ";
    private static final String RM_BUTTON = "-";
    private static final String BRACKET = ") ";

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

    public EditText getReps() {
        return reps;
    }


    public EditText getWeight() {
        return weight;
    }

    public void setWeight(EditText weight) {
        this.weight = weight;
    }

    /**
     * Adds a rep line to the card view, containing all the UI components
     * @param context       the application context
     * @param linearLayout	the linear layout to add the line to
     * @param count		    the set number
     * @param rep		    the target reps
     * @param model		    the object that is being displayed
     * @param isNew		    whether or not the line is being created from the plan or is additional
     */
    public static void addRepLine(Context context, LinearLayout linearLayout, int count, int rep,
                            WorkoutPlanExercise model, boolean isNew, boolean isPlan){

        String reps = TARGET_REPS;
        if(isNew){
            reps = REPS;
        }
        if(isPlan){
            reps = ENTER_TARGET_REPS;
        }

        //Horizontal linear layout so everything is on the same line
        LinearLayout hor = new LinearLayout(context);
        hor.setOrientation(LinearLayout.HORIZONTAL);

        linearLayout.addView(hor);

        //the set number
        TextView txt = new TextView(context);
        txt.setText((count) +BRACKET);

        //text box to enter weight
        EditText etWeight = new EditText(context);
        etWeight.setHint(WEIGHT);
        etWeight.setInputType(InputType.TYPE_CLASS_NUMBER);


        //text box to enter reps
        EditText et = new EditText(context);
        if(rep != 0)
            et.setHint(reps+rep);
        else et.setHint(reps);

        if(!isNew){
            et.setText(rep + "");
        }
        et.setInputType(InputType.TYPE_CLASS_NUMBER);

        //button to remove the line of data
        Button rm = new Button(context);
        rm.setText(RM_BUTTON);
        rm.setTextSize(14);
        rm.setBackgroundTintList(context.getResources().getColorStateList(R.color.dialog_color));

        //create rep line object as a way of storing all these components to access them
        //again later
        RepLine rl = new RepLine(txt, et, rm, hor);

        rl.setWeight(etWeight);

        //add all the rep lines to the workout
        model.getRepLines().add(rl);
        //This boolean is for when this rep line is being used to create a plan
        // and therefore this weight box is not needed
        hor.addView(txt);


        if(!isPlan) {
            rl.setWeight(etWeight);
            hor.addView(etWeight);
        }

        //remove the line of data when the remove button is clicked
        rm.setOnClickListener(view1 -> {
            linearLayout.removeView(hor);
            model.getRepLines().remove(rl);
        });

        //add all components to the horizontal view
        hor.addView(et);
        hor.addView(rm);

        count++;
    }
}
