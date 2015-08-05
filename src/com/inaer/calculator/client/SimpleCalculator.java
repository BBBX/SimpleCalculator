package com.inaer.calculator.client;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.inaer.calculator.shared.FieldVerifier;
import com.sencha.gxt.cell.core.client.ButtonCell.ButtonScale;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

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
	private TextButton clearBtn, clearEntryBtn, decimalBtn, signBtn, percentBtn, resultBtn, convertToBinaryBtn;
	private List<TextButton> numBtn = new ArrayList<TextButton>();
	private List<TextButton> operatorsBtn = new ArrayList<TextButton>();
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

		int minWidth = 80;

		// Set values to buttons
		signBtn = new TextButton("+/-");
		signBtn.setMinWidth(minWidth);
		signBtn.setScale(ButtonScale.LARGE);
		signBtn.setStylePrimaryName("btn");
		percentBtn = new TextButton("%");
		percentBtn.setScale(ButtonScale.LARGE);
		percentBtn.setMinWidth(minWidth);
		clearBtn = new TextButton("C");
		clearBtn.setScale(ButtonScale.LARGE);
		clearBtn.setMinWidth(minWidth);
		clearEntryBtn = new TextButton("CE");
		clearEntryBtn.setScale(ButtonScale.LARGE);
		clearEntryBtn.setMinWidth(minWidth);
		decimalBtn = new TextButton(".");
		decimalBtn.setScale(ButtonScale.LARGE);
		decimalBtn.setMinWidth(minWidth);
		resultBtn = new TextButton("=");
		resultBtn.setScale(ButtonScale.LARGE);
		resultBtn.setMinWidth(minWidth);
		convertToBinaryBtn = new TextButton("Dec -> Bin");
		convertToBinaryBtn.setScale(ButtonScale.LARGE);
		convertToBinaryBtn.setMinWidth(minWidth);
		for (int i = 0; i < operators.length; i++) {
			TextButton opBtn = new TextButton(operators[i]);
			opBtn.setScale(ButtonScale.LARGE);
			opBtn.setMinWidth(minWidth);
			operatorsBtn.add(opBtn);
		}
		for (int i = 0; i < 10; i++) {
			TextButton opBtn = new TextButton(String.valueOf(i));
			opBtn.setScale(ButtonScale.LARGE);
			opBtn.setMinWidth(minWidth);
			opBtn.setStylePrimaryName("opBtn");
			numBtn.add(opBtn);
		}

		// Default result value
		resultField.setText("0");
		resultField.setStylePrimaryName("result");

		// Container of the calculator
		FlexTable flexTable = new FlexTable();
		flexTable.setCellSpacing(10);
		flexTable.setStylePrimaryName("tableCalc");
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
		SelectHandler numButtonHandler = new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				Widget sender = (Widget) event.getSource();
				String pressed = sender.getElement().getInnerText();
				number(pressed);
			}
		};

		// Create a handler for the clearBtn

		SelectHandler clearButtonHandler = new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				resultField.setText("0");
				newLine = true;
				subresult = 0.0;
				lastOperator = "";
			}
		};

		// Create a handler for the clearBtn SelectHandler
		SelectHandler clearEntryButtonHandler = new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				resultField.setText("0");
				newLine = true;
			}
		};

		// Create a handler for the decimalBtn SelectHandler
		SelectHandler decimalButtonHandler = new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				decimal();
			}

		};

		// Create a handler for the signBtn
		SelectHandler signButtonHandler = new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				changeSign();
			}

		};

		// Create a handler for the percentBtn
		SelectHandler percentButtonHandler = new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				percent();
			}
		};

		// Create a handler for the operators
		SelectHandler operatorButtonHandler = new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				Widget sender = (Widget) event.getSource();
				operation();
				lastOperator = sender.getElement().getInnerText();
			}
		};

		// Create a handler for the convertToBinaryBtn
		SelectHandler convertToBinaryHandler = new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
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
		};

		// Set handlers
		clearBtn.addSelectHandler(clearButtonHandler);
		clearEntryBtn.addSelectHandler(clearEntryButtonHandler);
		resultBtn.addSelectHandler(operatorButtonHandler);
		signBtn.addSelectHandler(signButtonHandler);
		decimalBtn.addSelectHandler(decimalButtonHandler);
		percentBtn.addSelectHandler(percentButtonHandler);
		convertToBinaryBtn.addSelectHandler(convertToBinaryHandler);

		for (int i = 0; i < operators.length; i++) {
			operatorsBtn.get(i).addSelectHandler(operatorButtonHandler);
		}

		for (int i = 0; i < numBtn.size(); i++) {
			numBtn.get(i).addSelectHandler(numButtonHandler);
		}
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
			case "\r\n+":
				subresult += operand;
				break;
			case "\r\n-":
				subresult -= operand;
				break;
			case "\r\n*":
				subresult *= operand;
				break;
			case "\r\n/":
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
