package com.scra.codelabs.pid.commands.tuning;

import com.scra.codelabs.pid.subsystems.ShooterSubsystem;
import edu.wpi.first.wpilibj2.command.CommandBase;
import com.gos.lib.properties.PropertyManager;

public class FindShooterFFGainCommand extends CommandBase {

    private static final PropertyManager.IProperty<Double> SHOOTER_SPEED = PropertyManager.createDoubleProperty(false, "Tuning.Shooter.FFSpeed", 0);
    private final ShooterSubsystem m_shooter;

    public FindShooterFFGainCommand(ShooterSubsystem shooter) {
        m_shooter = shooter;
        addRequirements(m_shooter);
    }

    @Override
    public void execute() {
        m_shooter.setPercentOutput(SHOOTER_SPEED.getValue());
    }

}
