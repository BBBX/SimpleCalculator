package com.inaer.calculator.client;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.inaer.calculator.shared.FieldVerifier;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SimpleCalculator implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network " + "connection and try again.";
	private Button clearBtn, clearEntryBtn, decimalBtn, signBtn, percentBtn, resultBtn, convertToBinaryBtn;
	private List<Button> numBtn = new ArrayList<Button>();
	private List<Button> operatorsBtn = new ArrayList<Button>();
	private final String[] operators = { "+", "-", "*", "/" };
	private boolean newLine = false;
	private String lastOperator = "";
	private Double subresult = 0.0;
	private TextBox resultField = new TextBox();
	private Label errorLabel;

	/**
	 * Create a remote service proxy to talk to the server-side Calculator
	 * service.
	 */
	private final CalculatorServiceAsync calculatorService = GWT.create(CalculatorService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		errorLabel = new Label();

		// Set values to buttons
		signBtn = new Button("+/-");
		percentBtn = new Button("%");
		clearBtn = new Button("C");
		clearEntryBtn = new Button("CE");
		decimalBtn = new Button(".");
		resultBtn = new Button("=");
		convertToBinaryBtn = new Button("Dec -> Bin");
		for (int i = 0; i < operators.length; i++) {
			operatorsBtn.add(new Button(operators[i]));
		}
		for (int i = 0; i < 10; i++) {
			numBtn.add(new Button(String.valueOf(i)));
		}

		// Default result value
		resultField.setText("0");

		// Container of the calculator
		FlexTable flexTable = new FlexTable();
		flexTable.setWidget(0, 0, resultField);
		flexTable.setWidget(0, 1, clearBtn);
		flexTable.setWidget(0, 2, clearEntryBtn);

		flexTable.setWidget(1, 0, numBtn.get(7));
		flexTable.setWidget(1, 1, numBtn.get(8));
		flexTable.setWidget(1, 2, numBtn.get(9));
		flexTable.setWidget(1, 3, signBtn);
		flexTable.setWidget(1, 4, percentBtn);

		flexTable.setWidget(2, 0, numBtn.get(4));
		flexTable.setWidget(2, 1, numBtn.get(5));
		flexTable.setWidget(2, 2, numBtn.get(6));
		flexTable.setWidget(2, 3, operatorsBtn.get(0));
		flexTable.setWidget(2, 4, operatorsBtn.get(1));

		flexTable.setWidget(3, 0, numBtn.get(1));
		flexTable.setWidget(3, 1, numBtn.get(2));
		flexTable.setWidget(3, 2, numBtn.get(3));
		flexTable.setWidget(3, 3, operatorsBtn.get(2));
		flexTable.setWidget(3, 4, operatorsBtn.get(3));

		flexTable.setWidget(4, 0, numBtn.get(0));
		flexTable.setWidget(4, 1, decimalBtn);
		flexTable.setWidget(4, 2, convertToBinaryBtn);
		flexTable.setWidget(4, 3, resultBtn);

		flexTable.getFlexCellFormatter().setColSpan(0, 0, 3);
		flexTable.getFlexCellFormatter().setColSpan(4, 2, 2);

		// Add elements to containers in HTML page
		RootPanel.get("errorLabelContainer").add(errorLabel);
		RootPanel.get("calcContainer").add(flexTable);

		// Focus the cursor on the result field when the app loads
		resultField.setFocus(true);
		resultField.selectAll();

		// Create a handler for the numBtn
		class NumButtonHandler implements ClickHandler {
			/**
			 * Fired when the user clicks on one numBtn.
			 */
			public void onClick(ClickEvent event) {
				Widget sender = (Widget) event.getSource();
				String pressed = sender.getElement().getInnerText();
				number(pressed);
			}
		}

		// Create a handler for the clearBtn
		class ClearButtonHandler implements ClickHandler {
			/**
			 * Fired when the user clicks on clear numBtn.
			 */
			public void onClick(ClickEvent event) {
				resultField.setText("0");
				newLine = true;
				subresult = 0.0;
				lastOperator = "";
			}
		}

		// Create a handler for the clearBtn
		class ClearEntryButtonHandler implements ClickHandler {
			/**
			 * Fired when the user clicks on one numBtn.
			 */
			public void onClick(ClickEvent event) {
				resultField.setText("0");
				newLine = true;
			}
		}

		// Create a handler for the decimalBtn
		class DecimalButtonHandler implements ClickHandler {
			/**
			 * Fired when the user clicks on one numBtn.
			 */
			public void onClick(ClickEvent event) {
				decimal();
			}
		}

		// Create a handler for the signBtn
		class SignButtonHandler implements ClickHandler {
			/**
			 * Fired when the user clicks on one numBtn.
			 */
			public void onClick(ClickEvent event) {
				changeSign();
			}
		}

		// Create a handler for the percentBtn
		class PercentButtonHandler implements ClickHandler {
			/**
			 * Fired when the user clicks on one numBtn.
			 */
			public void onClick(ClickEvent event) {
				percent();
			}
		}

		// Create a handler for the operators
		class OperatorButtonHandler implements ClickHandler {
			/**
			 * Fired when the user clicks on operator button.
			 */
			public void onClick(ClickEvent event) {
				Widget sender = (Widget) event.getSource();
				operation();
				lastOperator = sender.getElement().getInnerText();
			}
		}

		// Create a handler for the convertToBinaryBtn
		class ConvertToBinaryHandler implements ClickHandler {
			/**
			 * Fired when the user clicks on the convert to binary button.
			 */
			public void onClick(ClickEvent event) {
				sendValueToServer();
			}

			/**
			 * Send the value of the result field to the server and wait for a
			 * response containing the value converted to binary representation.
			 */
			private void sendValueToServer() {
				errorLabel.setText("");
				String valueToServer = resultField.getText();

				if (!FieldVerifier.isValidNumber(valueToServer)) {
					errorLabel.setText("Wrong number format");
					return;
				}

				convertToBinaryBtn.setEnabled(false);

				calculatorService.convertToBinary(valueToServer, new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						errorLabel.setText(SERVER_ERROR);
					}

					public void onSuccess(String result) {
						resultField.setText(result);
						convertToBinaryBtn.setEnabled(true);
						newLine = true;
					}
				});
			}
		}

		// Set handlers
		ClearButtonHandler clearButtonHandler = new ClearButtonHandler();
		clearBtn.addClickHandler(clearButtonHandler);

		ClearEntryButtonHandler clearEntryButtonHandler = new ClearEntryButtonHandler();
		clearEntryBtn.addClickHandler(clearEntryButtonHandler);

		OperatorButtonHandler operatorButtonHandler = new OperatorButtonHandler();
		for (int i = 0; i < operators.length; i++) {
			operatorsBtn.get(i).addClickHandler(operatorButtonHandler);
		}
		resultBtn.addClickHandler(operatorButtonHandler);

		NumButtonHandler numButtonHandler = new NumButtonHandler();
		for (int i = 0; i < numBtn.size(); i++) {
			numBtn.get(i).addClickHandler(numButtonHandler);
		}

		SignButtonHandler signButtonHandler = new SignButtonHandler();
		signBtn.addClickHandler(signButtonHandler);

		DecimalButtonHandler decimalButtonHandler = new DecimalButtonHandler();
		decimalBtn.addClickHandler(decimalButtonHandler);

		PercentButtonHandler percentButtonHandler = new PercentButtonHandler();
		percentBtn.addClickHandler(percentButtonHandler);

		// Add a handler to send the number to convert to the server
		ConvertToBinaryHandler convertToBinaryHandler = new ConvertToBinaryHandler();
		convertToBinaryBtn.addClickHandler(convertToBinaryHandler);
	}

	/**
	 * Add a decimal separator to current number in result field.
	 */
	private void decimal() {
		if (newLine) {
			resultField.setText("0.");
			newLine = false;
		} else {
			String inputText = resultField.getText();
			if (inputText.indexOf(".") == -1) {
				inputText += ".";
				resultField.setText(inputText);
			}
		}
	}

	/**
	 * Performs an operation using the accumulated value and the current value
	 * of the result field.
	 */
	private void operation() {
		if (newLine && lastOperator != "=") {

		} else {
			Double operand = Double.parseDouble(resultField.getText());

			switch (lastOperator) {
			case "+":
				subresult += operand;
				break;
			case "-":
				subresult -= operand;
				break;
			case "*":
				subresult *= operand;
				break;
			case "/":
				subresult /= operand;
				break;
			default:
				subresult = operand;
			}

			BigDecimal result = new BigDecimal(String.valueOf(subresult));
			resultField.setText(result.stripTrailingZeros().toPlainString());
			newLine = true;
		}
	}

	/**
	 * Inserts the pressed number to the result field. If current value of
	 * result field is zero or the flag of new line is activated, the number
	 * will be concatenated with the current value.
	 */
	private void number(String pressed) {
		if (newLine || resultField.getText().equals("0")) {
			resultField.setText(pressed);
			newLine = false;
		} else {
			resultField.setText(resultField.getText() + pressed);
		}
	}

	/**
	 * Calculates the percentage value of the accumulated result with the value
	 * of the result field.
	 */
	private void percent() {
		Double value = (Double.parseDouble(resultField.getText()) / 100) * subresult;
		BigDecimal output = new BigDecimal(String.valueOf(value));
		resultField.setText(output.stripTrailingZeros().toPlainString());
	}

	/**
	 * Inverts the sign of the current value.
	 */
	private void changeSign() {
		BigDecimal output = new BigDecimal(String.valueOf(Double.parseDouble(resultField.getText()) * -1));
		resultField.setText(output.stripTrailingZeros().toPlainString());
	}
}
