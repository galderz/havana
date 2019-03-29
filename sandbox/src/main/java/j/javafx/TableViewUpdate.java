package j.javafx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.stage.Stage;

import java.util.ArrayList;

public class TableViewUpdate extends Application {

   public static void main(String[] args) throws Exception { launch(args); }
   public void start(final Stage stage) throws Exception {
      final Label statusLabel = new Label("Status");
      final Label numberLabel =  new Label("Empty");
      final Button runButton = new Button("Run");
      final ListView<String> peopleView = new ListView<String>();
      peopleView.setPrefSize(220, 162);
      final ProgressBar progressBar = new ProgressBar();
      progressBar.prefWidthProperty().bind(peopleView.prefWidthProperty());

      runButton.setOnAction(new EventHandler<ActionEvent>() {
         @Override public void handle(ActionEvent actionEvent) {
            PartialResultsTask task = new PartialResultsTask();
//                ResultTask task = new ResultTask();

            statusLabel.textProperty().bind(task.messageProperty());
            runButton.disableProperty().bind(task.runningProperty());
            peopleView.itemsProperty().bind(task.partialResultsProperty());
//                peopleView.itemsProperty().bind(task.valueProperty());
            progressBar.progressProperty().bind(task.progressProperty());
            task.stateProperty().addListener(new ChangeListener<Worker.State>() {
               @Override public void changed(ObservableValue<? extends Worker.State> observableValue, Worker.State oldState, Worker.State newState) {
                  if (newState == Worker.State.RUNNING) {
                     System.out.println("This is ok, this thread " + Thread.currentThread() + " is the JavaFX Application thread.");
                     runButton.setText("Voila!");
                  }
               }
            });

            new Thread(task).start();
         }
      });

      final VBox layout =
            VBoxBuilder.create().spacing(8).children(
                  VBoxBuilder.create().spacing(5).children(
                        HBoxBuilder.create().spacing(10).children(
                              runButton,
                              statusLabel).build(),
                        progressBar
                  ).build(),
                  peopleView
            ).build();
      layout.setStyle("-fx-background-color: cornsilk; -fx-padding:10; -fx-font-size: 16;");
      Scene scene = new Scene(layout);
      stage.setScene(scene);
      stage.show();
   }

   public class PartialResultsTask extends Task<ObservableList<String>> {
      // Uses Java 7 diamond operator
      private ReadOnlyObjectWrapper<ObservableList<String>> partialResults =
            new ReadOnlyObjectWrapper<>(this, "partialResults",
                  FXCollections.observableArrayList(new ArrayList<String>()));

      public final ObservableList getPartialResults() { return partialResults.get(); }
      public final ReadOnlyObjectProperty partialResultsProperty() {
         return partialResults.getReadOnlyProperty();
      }

      @Override protected ObservableList<String> call() throws InterruptedException {
         updateMessage("Finding friends . . .");
         long startTime = System.currentTimeMillis();
         for (int i = 0; i < 10000; i++) {
            final int finalI = i;
            Platform.runLater(new Runnable() {
               @Override
               public void run() {
                  partialResults.get().add(String.valueOf(finalI));
               }
            });
            Thread.sleep(2);
//                updateProgress(i + 1, 100);
         }
         updateMessage("Finished.");
         long finishTime = System.currentTimeMillis() - startTime;
         System.out.println(finishTime);
         return partialResults.get();
      }
   }
   public class ResultTask extends Task<ObservableList<String>> {
//        private ReadOnlyObjectWrapper<ObservableList<String>> partialResults =
//                new ReadOnlyObjectWrapper<>(this, "partialResults",
//                        FXCollections.observableArrayList(new ArrayList<String>()));

      @Override protected ObservableList<String> call() throws InterruptedException {
         ObservableList<String> list = FXCollections.observableArrayList(new ArrayList<String>());
         updateMessage("Finding friends . . .");
         long startTime = System.currentTimeMillis();

         for (int i = 0; i < 10000; i++) {
            list.add(String.valueOf(i));
            Thread.sleep(2);
//                updateProgress(i+1, 10);
         }
         updateMessage("Finished.");
         long finishTime = System.currentTimeMillis() - startTime;
         System.out.println(finishTime);
         return list;
      }
   }

}
