package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;

import org.firstinspires.ftc.teamcode.components.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.components.commands.ShooterCommand;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Shooter TeleOp", group = "Competition")
public class ShooterTeleOp extends CommandOpMode {

    private ShooterSubsystem shooterSubsystem;
    private ShooterCommand shooterRPMCommand;
    private ShooterCommand shooterPowerCommand;

    private boolean shooterRunning = false;
    private boolean useRPMControl = true;
    private double targetRPM = 6000;   // Default shooting speed
    private double shooterPower = 0.75; // Backup open-loop power

    @Override
    public void initialize() {
        telemetry.addLine("Initializing Shooter TeleOp...");
        telemetry.update();

        shooterSubsystem = new ShooterSubsystem(hardwareMap, "shooterMotor");

        // Create both command options
        shooterRPMCommand = new ShooterCommand(shooterSubsystem, targetRPM);
        shooterPowerCommand = new ShooterCommand(shooterSubsystem, shooterPower, false);

        telemetry.addLine("Shooter TeleOp Initialized!");
        telemetry.update();
    }

    @Override
    public void run() {
        super.run(); // required for CommandScheduler

        double rightTrigger = gamepad2.right_trigger;
        double leftTrigger = gamepad2.left_trigger;

        // --- Control shooter with triggers ---
        if (rightTrigger > 0.15 && !shooterRunning) {
            // Start shooter
            if (useRPMControl) {
                CommandScheduler.getInstance().schedule(shooterRPMCommand);
            } else {
                CommandScheduler.getInstance().schedule(shooterPowerCommand);
            }
            shooterRunning = true;
        } 
        else if (leftTrigger > 0.15 && shooterRunning) {
            // Stop shooter
            CommandScheduler.getInstance().cancelAll();
            shooterSubsystem.stopShooter();
            shooterRunning = false;
        }

        // --- Toggle control mode with X ---
        if (gamepad2.x) {
            useRPMControl = !useRPMControl;
            sleep(300); // debounce
        }

        // --- Adjust target RPM up/down ---
        if (gamepad2.dpad_up) {
            targetRPM += 250;
            shooterRPMCommand = new ShooterCommand(shooterSubsystem, targetRPM);
            sleep(200);
        } else if (gamepad2.dpad_down) {
            targetRPM = Math.max(500, targetRPM - 250);
            shooterRPMCommand = new ShooterCommand(shooterSubsystem, targetRPM);
            sleep(200);
        }

        // --- Telemetry ---
        telemetry.addData("Shooter Mode", useRPMControl ? "RPM Control" : "Power Control");
        telemetry.addData("Shooter Running", shooterRunning);
        telemetry.addData("Target RPM", targetRPM);
        telemetry.addData("Current RPM", shooterSubsystem.getCurrentRPM());
        telemetry.addData("PIDF", shooterSubsystem.getPIDFCoefficients().toString());
        telemetry.update();
    }
}
