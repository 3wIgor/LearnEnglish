package com.learn.viewcontroller;

import com.learn.dao.Repository;
import com.learn.daoimpl.RepositoryFromFiles;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ResourceBundle;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FXMLController implements Initializable {
    
    private static final Logger logger = LoggerFactory.getLogger(FXMLController.class);
    private final Repository repository = new RepositoryFromFiles();
    private final LessonWords lessonWords = new LessonWords(repository);
    private final Queue<String> qTextLines = new LinkedList<>();
    private final Popup popup = new Popup();
    {
        popup.setAutoHide(true);
    }
    
    @FXML
    private TextField textFildEnglishWord;
    @FXML
    private TextField textFildRussianWord;
    @FXML
    private ComboBox<String> comboBoxLessonWord;
    @FXML
    private ComboBox<String> comboBoxText;
    @FXML
    private ListView<String> listViewRussianWords;
    @FXML
    private Button buttonTransleteWord;
    @FXML
    private Button buttonRefreshLessonWord;
    @FXML
    private TextArea textAreaLines;
    @FXML
    private TextField textFildLine;
    @FXML
    private TabPane tabPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        logger.debug("args url: {} ResourceBundle: {}", new Object[]{url, rb});
        comboBoxLessonWord.getItems().addAll(repository.getLessonsNames());
        if (!comboBoxLessonWord.getItems().isEmpty()) {
            comboBoxLessonWord.getSelectionModel().select(0);
            comboBoxLessonWordOnAction();
        }

        comboBoxText.getItems().addAll(repository.getTextsNames());
        if (!comboBoxText.getItems().isEmpty()) {
            comboBoxText.getSelectionModel().select(0);
            comboBoxTextOnAction();
        }
    }
    
    private void startLessonWord() {
        logger.debug(null);
        textFildEnglishWord.setEditable(true);
        setWord();
        buttonTransleteWord.setDisable(false);
        textFildEnglishWord.requestFocus();
    }

    private void setWord() {
        logger.debug(null);
        textFildEnglishWord.setText("");
        textFildRussianWord.setText(lessonWords.carentWord());
        listViewRussianWords.getSelectionModel().select(lessonWords.carentWord());
        listViewRussianWords.scrollTo(listViewRussianWords.getSelectionModel().getSelectedIndex());
    }

    @FXML
    private void textFildEnglishWordOnKeyPressed(KeyEvent event) {
        logger.debug("arg KeyEvent: {}", event);
        if (event.getCode() == KeyCode.ENTER) {
            if (lessonWords.ckeckWord(textFildEnglishWord.getText())) {
                if(popup.isShowing()) popup.hide();
                if (lessonWords.nextWord()) {
                    setWord();
                } else {
                    textFildRussianWord.setText("");
                    textFildEnglishWord.setText("");
                    textFildEnglishWord.setEditable(false);
                    showInfo("Finish");
                    buttonTransleteWord.setDisable(true);
                    buttonRefreshLessonWord.requestFocus();
                }
            } else {
                textFildEnglishWord.setStyle("-fx-text-fill: red");
            }
        } else {
            textFildEnglishWord.setStyle("-fx-text-fill: black");
        }
    }


    @FXML
    private void comboBoxLessonWordOnAction() {
        logger.debug(null);
        lessonWords.setLesson(comboBoxLessonWord.getSelectionModel().getSelectedItem());
        listViewRussianWords.getItems().clear();
        listViewRussianWords.getItems().addAll(lessonWords.getAllRusianWords());
        startLessonWord();
    }

    @FXML
    public void buttonRefreshLessonWordClick() {
        logger.debug(null);
        lessonWords.refrechLesson();
        comboBoxLessonWordOnAction();
    }


    @FXML
    private void textFildLinedOnKeyPressed(KeyEvent event) {
        logger.debug("arg KeyEvent: {}", event);
        if (event.getCode() == KeyCode.ENTER) {
            if (textFildLine.getText().equalsIgnoreCase(qTextLines.peek())) {
                qTextLines.remove();
                if (!qTextLines.isEmpty()) {
                    textAreaLines.setText(textAreaLines.getText() + " " + qTextLines.peek());
                    textAreaLines.selectRange(textAreaLines.getLength() - qTextLines.peek().length(), textAreaLines.getLength());
                    textFildLine.setText("");
                } else {
                    textFildLine.setText("");
                    showInfo("Finish");
                    textFildLine.setDisable(false);
                }
            } else {
                textFildLine.setStyle("-fx-text-fill: red");
            }
        } else {
            textFildLine.setStyle("-fx-text-fill: black");
        }
    }

    @FXML
    private void buttonTransleteWordClick(MouseEvent evt) {
        logger.debug("arg MouseEvent: {}", evt);
        Text text = new Text(lessonWords.getEnglishWord(textFildRussianWord.getText()));
        popup.getContent().clear();
        popup.getContent().add(text);
        Window w = textFildEnglishWord.getScene().getWindow();
        popup.show(textFildEnglishWord, w.getX() + 30, w.getY() + 150);
        textFildEnglishWord.requestFocus();
    }

    @FXML
    private void comboBoxTextOnAction() {
        logger.debug(null);
        qTextLines.clear();
        qTextLines.addAll(Arrays.asList(repository.getLinesOfText(comboBoxText.getSelectionModel().getSelectedItem())));
        textAreaLines.setText(qTextLines.peek());
        textFildLine.setDisable(false);
        textFildLine.requestFocus();
    }

    private void showInfo(String content) {
        logger.debug("arg content: {}", content);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
