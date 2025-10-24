package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;

import org.firstinspires.ftc.teamcode.components.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.components.commands.IntakeCommand;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Intake TeleOp", group = "Competition")
public class IntakeTeleop extends CommandOpMode {

    private IntakeSubsystem intakeSubsystem;
    private IntakeCommand intakeForwardCommand;
    private IntakeCommand intakeReverseCommand;

    private boolean intakeRunning = false;
    private boolean intakeForward = true; // true = forward, false = reverse
    private double intakePower = 1.0; // Default power

    @Override
    public void initialize() {
        telemetry.addLine("Initializing Intake TeleOp...");
        telemetry.update();

        // Initialize intake subsystem
        intakeSubsystem = new IntakeSubsystem(hardwareMap, "intakeMotor");

        // Create commands for forward and reverse
        intakeForwardCommand = new IntakeCommand(intakeSubsystem, intakePower);
        intakeReverseCommand = new IntakeCommand(intakeSubsystem, -intakePower);

        telemetry.addLine("Intake TeleOp Initialized!");
        telemetry.update();
    }

    @Override
    public void run() {
        super.run(); // Required for CommandScheduler

        boolean rightBumper = gamepad2.right_bumper;
        boolean leftBumper = gamepad2.left_bumper;

        // --- Control intake with bumpers ---
        if (rightBumper && !intakeRunning) {
            // Start intake forward
            CommandScheduler.getInstance().schedule(intakeForwardCommand);
            intakeRunning = true;
            intakeForward = true;
        } 
        else if (leftBumper && !intakeRunning) {
            // Start intake in reverse
            CommandScheduler.getInstance().schedule(intakeReverseCommand);
            intakeRunning = true;
            intakeForward = false;
        } 
        else if (!rightBumper && !leftBumper && intakeRunning) {
            // Stop intake when no bumper is pressed
            CommandScheduler.getInstance().cancelAll();
            intakeSubsystem.setIntake(0);
            intakeRunning = false;
        }

        // --- Telemetry ---
        telemetry.addData("Intake Running", intakeRunning);
        telemetry.addData("Intake Direction", intakeForward ? "Forward" : "Reverse");
        telemetry.addData("Intake Power", intakePower);
        telemetry.update();
    }
}
