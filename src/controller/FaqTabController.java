package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import lombok.extern.slf4j.Slf4j;

import static data.TextConstants.*;

@Slf4j
public class FaqTabController {
    @FXML
    private TextArea introduction;
    @FXML
    private TextArea prepairingToWork;
    @FXML
    private TextArea workInstruction;
    @FXML
    private TextArea saveAndLoadInstruction;
    @FXML
    private TextArea feedbackText;

    @FXML
    void initialize() {
        log.info("инициализация.");
        fillFaqText();
    }

    @FXML
    void fillFaqText() {
        introduction.setText(INTRODUCTION_FAQ);
        prepairingToWork.setText(PREPAIRING_TO_WORK_FAQ);
        workInstruction.setText(WORK_INSTRUCTIONS_FAQ);
        saveAndLoadInstruction.setText(SAVE_AND_LOAD_FAQ);
        feedbackText.setText(CONTACTS_AND_FEEDBACK_FAQ);
    }
}

