package com.scra.codelabs.pid;

import com.scra.codelabs.pid.commands.MovePunchCommand;
import com.scra.codelabs.pid.subsystems.PunchSubsystem;
import com.scra.codelabs.pid.auton_modes.TrajectoryFactory;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.math.util.Units;
import com.scra.codelabs.pid.commands.ElevatorToPositionCommand;
import com.scra.codelabs.pid.commands.ShooterRpmCommand;
import com.scra.codelabs.pid.commands.auton.DriveStraightDistanceCustomControlCommand;
import com.scra.codelabs.pid.commands.auton.DriveStraightDistancePositionControlCommand;
import com.scra.codelabs.pid.commands.auton.DriveStraightDistanceSmartMotionControlCommand;
import com.scra.codelabs.pid.commands.auton.SetRobotPoseCommand;
import com.scra.codelabs.pid.commands.tuning.FindChassisTurningCompensationCommand;
import com.scra.codelabs.pid.commands.tuning.FindElevatorGravityCompensationCommand;
import com.scra.codelabs.pid.commands.tuning.FindShooterFFGainCommand;
import com.scra.codelabs.pid.commands.tuning.TuneChassisVelocityCommand;
import com.scra.codelabs.pid.commands.tuning.TuneShooterRpmCommand;
import com.scra.codelabs.pid.subsystems.ChassisSubsystem;
import com.scra.codelabs.pid.subsystems.ElevatorSubsystem;
import com.scra.codelabs.pid.subsystems.ShooterSubsystem;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class CommandTester {

    public CommandTester(RobotContainer robotContainer) {

        // Alias for readability
        ChassisSubsystem chassis = robotContainer.getChassis();
        ElevatorSubsystem elevator = robotContainer.getElevator();
        ShooterSubsystem shooter = robotContainer.getShooter();
        PunchSubsystem punch = robotContainer.getPunch();

        ////////////////////////////////
        // Teleop
        ////////////////////////////////

        // Elevator
        addCommand("Lift To Position Low", new ElevatorToPositionCommand(elevator, ElevatorSubsystem.Positions.LOW));
        addCommand("Lift To Position Mid", new ElevatorToPositionCommand(elevator, ElevatorSubsystem.Positions.MID));
        addCommand("Lift To Position High", new ElevatorToPositionCommand(elevator, ElevatorSubsystem.Positions.HIGH));

        // Punch
        addCommand("Test Extend Punch", new MovePunchCommand(punch, true));
        addCommand("Test Retract Punch", new MovePunchCommand(punch, false));

        // Shooter
        addCommand("Shooter RPM 1500", new ShooterRpmCommand(shooter, 1500));
        addCommand("Shooter RPM 2000", new ShooterRpmCommand(shooter, 2000));

        ////////////////////////////////
        // Autonomous
        ////////////////////////////////

        // Tuning
        addCommand("Tuning.ChassisTurningComp", new FindChassisTurningCompensationCommand(chassis));
        addCommand("Tuning.ChassisVelocity", new TuneChassisVelocityCommand(chassis));
        addCommand("Tuning.ElevatorGravityComp", new FindElevatorGravityCompensationCommand(elevator));
        addCommand("Tuning.ShooterFF", new FindShooterFFGainCommand(shooter));
        addCommand("Tuning.ShooterRPM", new TuneShooterRpmCommand(shooter));

        // Position Driving
        double driveDistanceCommand = 180;
        addCommand("Drive Distance (Custom) Forwards", new DriveStraightDistanceCustomControlCommand(chassis, Units.inchesToMeters(driveDistanceCommand)));
        addCommand("Drive Distance (Custom) Backwards", new DriveStraightDistanceCustomControlCommand(chassis, Units.inchesToMeters(-driveDistanceCommand)));
        addCommand("Drive Distance (Position) Forwards", new DriveStraightDistancePositionControlCommand(chassis, Units.inchesToMeters(driveDistanceCommand)));
        addCommand("Drive Distance (Position) Backwards", new DriveStraightDistancePositionControlCommand(chassis, Units.inchesToMeters(-driveDistanceCommand)));
        addCommand("Drive Distance (SM) Forwards", new DriveStraightDistanceSmartMotionControlCommand(chassis, Units.inchesToMeters(driveDistanceCommand)));
        addCommand("Drive Distance (SM) Backwards", new DriveStraightDistanceSmartMotionControlCommand(chassis, Units.inchesToMeters(-driveDistanceCommand)));

        // Trajectory Driving
        addCommand("SetStartingPosition (-90)", new SetRobotPoseCommand(chassis, new Pose2d(2, 5, Rotation2d.fromDegrees(-90))));
        addCommand("SetStartingPosition (0)", new SetRobotPoseCommand(chassis, new Pose2d(2, 5, Rotation2d.fromDegrees(0))));
        addCommand("SetStartingPosition (90)", new SetRobotPoseCommand(chassis, new Pose2d(2, 5, Rotation2d.fromDegrees(90))));
        addCommand("Trajectory.Straight Forward", TrajectoryFactory.getTestStraightForwardTestTrajectory(chassis));
        addCommand("Trajectory.Straight Backwards", TrajectoryFactory.getTestStraightBackwardsTestTrajectory(chassis));
        addCommand("Trajectory.Across Field", TrajectoryFactory.getTestStraightAcrossFieldTrajectory(chassis));
        addCommand("Trajectory.S-Curve", TrajectoryFactory.getTestSCurveTrajectory(chassis));

    }

    private void addCommand(String name, CommandBase command) {
        CommandBase namedCommand =  command.andThen(() -> SmartDashboard.putBoolean("Auton Running", false)).withName(name);
        Shuffleboard.getTab("Command Tester").add(name, namedCommand);
    }
}
