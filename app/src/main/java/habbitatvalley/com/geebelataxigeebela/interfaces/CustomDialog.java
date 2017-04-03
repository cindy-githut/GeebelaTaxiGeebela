package habbitatvalley.com.geebelataxigeebela.interfaces;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import habbitatvalley.com.geebelataxigeebela.R;

public class CustomDialog extends Dialog implements DialogInterface.OnCancelListener, View.OnClickListener {

    public static final String LEFT_BUTTON = "Left Button";
    public static final String RIGHT_BUTTON = "Right Button";

    private Activity activity;
    private Dialog dialog;

    public TextView rightButton;
    private TextView leftButton;
    private TextView titleTextView;
    private TextView messageTextView;

    private int numberOfButtons = 2;

    private String buttonClicked;

    private static String rightButtonText;
    private static String leftButtonText;
    private static String titleViewText;
    private static String messageViewText;


    /**
     * This is the main Constructor of the class.
     * This constructor initializes the buttons with the default labels of 'Yes' and 'No'
     *
     * @param activity - The Context Activity
     */
    public CustomDialog(Activity activity) {
        super(activity);
        this.activity = activity;
        this.rightButtonText = "YES";
        this.leftButtonText = "NO";
    }

    /**
     * This is used for a single-button dialog
     *
     * @param activity - The Context Activity
     * @param singleButtonText - The text to display on the single button label
     */
    public CustomDialog(Activity activity, String singleButtonText) {
        super(activity);
        this.activity = activity;
        this.numberOfButtons = 1;
        this.rightButtonText = singleButtonText;
    }

    /**
     * This is used for a two-button dialog with the text of both buttons labels passed in
     *
     * @param activity - The Context Activity
     * @param leftButtonText - The text to display on the left button label
     * @param rightButtonText - The text to display on the right button label
     */
    public CustomDialog(Activity activity, String leftButtonText, String rightButtonText) {
        super(activity);
        this.activity = activity;
        this.leftButtonText = leftButtonText;
        this.rightButtonText = rightButtonText;
    }

    /**
     * This is used for a two-button dialog with the text of the title, message, and the single
     * button label passed in
     *
     * @param activity - The Context Activity
     * @param titleViewText - The text to display on the title of the dialog
     * @param messageViewText - The text of the message to display
     * @param rightButtonText - The text to display on the right button label
     */
    public CustomDialog(Activity activity, String titleViewText, String messageViewText,
                        String rightButtonText) {
        super(activity);
        this.activity = activity;
        this.numberOfButtons = 1;
        this.titleViewText = titleViewText;
        this.messageViewText =  messageViewText;
        this.rightButtonText = rightButtonText;
    }

    /**
     * This is used for a two-button dialog with the text of the title, message, and both buttons
     * labels passed in
     *
     * @param activity - The Context Activity
     * @param titleViewText - The text to display on the title of the dialog
     * @param messageViewText - The text of the message to display
     * @param leftButtonText - The text to display on the left button label
     * @param rightButtonText - The text to display on the right button label
     */
    public CustomDialog(Activity activity, String titleViewText, String messageViewText,
                        String leftButtonText, String rightButtonText) {
        super(activity);
        this.activity = activity;
        this.titleViewText = titleViewText;
        this.messageViewText =  messageViewText;
        this.leftButtonText = leftButtonText;
        this.rightButtonText = rightButtonText;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);

//        dialog.setCancelable(false);

        rightButton = (TextView) findViewById(R.id.txtRightButton);
        this.setRightButtonText(this.rightButtonText);
        rightButton.setOnClickListener(this);

        leftButton = (TextView) findViewById(R.id.txtLeftButton);
        if (this.numberOfButtons > 1) {
            this.setLeftButtonText(this.leftButtonText);
            leftButton.setOnClickListener(this);
        } else {
            // Remove left button as it is not needed
            // on a single-button dialog
            leftButton.setVisibility(View.GONE);
        }

        titleTextView = (TextView) findViewById(R.id.txtViewTitle);
        messageTextView = (TextView) findViewById(R.id.txtViewMessage);
        this.setCustomTitle(titleViewText);
        this.setCustomMessage(messageViewText);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);

    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        Log.i("Custom Dialog", "Dialog Cancelled");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtLeftButton:
                buttonClicked = LEFT_BUTTON;
                cancel();

                break;
            case R.id.txtRightButton:
                buttonClicked = RIGHT_BUTTON;
                dismiss();
                break;

            default:
                break;
        }
        dismiss();
    }

    public void setRightButtonText(String text) {
        this.rightButton.setText(text);
    }

    public void setLeftButtonText(String text) {
        this.leftButton.setText(text);
    }

    public void setCustomTitle(String headerViewText) {
        this.titleTextView.setText(headerViewText);
    }

    public void setCustomMessage(String messageViewText) {
        this.messageTextView.setText(messageViewText);
    }

    public String getRightButtonText() {
        return rightButtonText;
    }

    public String getLeftButtonText() {
        return leftButtonText;
    }

    public static String getCustomTitle() {
        return titleViewText;
    }

    public static String getCustomMessage() {
        return messageViewText;
    }

    public String getButtonClicked() {
        return buttonClicked;
    }
}
