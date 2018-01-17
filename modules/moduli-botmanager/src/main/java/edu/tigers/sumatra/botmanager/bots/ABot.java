/*
 * Copyright (c) 2009 - 2017, DHBW Mannheim - TIGERs Mannheim
 */

package edu.tigers.sumatra.botmanager.bots;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import com.github.g3force.configurable.IConfigObserver;

import edu.tigers.sumatra.bot.EBotType;
import edu.tigers.sumatra.bot.EFeature;
import edu.tigers.sumatra.bot.EFeatureState;
import edu.tigers.sumatra.bot.IBot;
import edu.tigers.sumatra.botmanager.basestation.IBaseStation;
import edu.tigers.sumatra.botmanager.commands.ACommand;
import edu.tigers.sumatra.botmanager.commands.MatchCommand;
import edu.tigers.sumatra.ids.BotID;
import edu.tigers.sumatra.ids.ETeamColor;
import edu.tigers.sumatra.math.vector.IVector3;
import edu.tigers.sumatra.trajectory.TrajectoryWithTime;


/**
 * Bot base class.
 * 
 * @author AndreR
 */
public abstract class ABot implements IBot, IConfigObserver
{
	private static final String[] BOT_NAMES = { "Gandalf", "Alice", "Tigger",
			"Poller",
			"Q", "Eichbaum",
			"This Bot",
			"Black Betty",
			"Trinity", "Neo",
			"Bob",
			"Yoda" };
	
	private final BotID botId;
	private final EBotType type;
	private final transient IBaseStation baseStation;
	private final Map<EFeature, EFeatureState> botFeatures;
	private final transient MatchCommand matchCtrl = new MatchCommand();
	private final transient List<IABotObserver> observers = new CopyOnWriteArrayList<>();
	private transient TrajectoryWithTime<IVector3> curTrajectory = null;
	private transient double kickerLevelMax = 200;
	private transient String controlledBy = "";
	private transient boolean hideFromRcm = false;
	
	/** [Hz] desired number of package to receive */
	private transient double updateRate = 100;
	
	
	/**
	 * @param type
	 * @param id
	 * @param baseStation
	 */
	public ABot(final EBotType type, final BotID id, final IBaseStation baseStation)
	{
		botId = id;
		this.type = type;
		botFeatures = getDefaultFeatureStates();
		this.baseStation = baseStation;
	}
	
	
	protected ABot(final ABot aBot, final EBotType type)
	{
		botId = aBot.botId;
		this.type = type;
		botFeatures = aBot.botFeatures;
		baseStation = aBot.baseStation;
		kickerLevelMax = aBot.kickerLevelMax;
	}
	
	
	protected ABot()
	{
		botId = null;
		type = null;
		botFeatures = null;
		baseStation = null;
	}
	
	
	/**
	 * @param observer
	 */
	public void addObserver(final IABotObserver observer)
	{
		observers.add(observer);
	}
	
	
	/**
	 * @param observer
	 */
	public void removeObserver(final IABotObserver observer)
	{
		observers.remove(observer);
	}
	
	
	protected void notifyIncommingBotCommand(final ACommand cmd)
	{
		for (IABotObserver observer : observers)
		{
			observer.onIncommingBotCommand(cmd);
		}
	}
	
	
	protected Map<EFeature, EFeatureState> getDefaultFeatureStates()
	{
		Map<EFeature, EFeatureState> result = EFeature.createFeatureList();
		result.put(EFeature.DRIBBLER, EFeatureState.WORKING);
		result.put(EFeature.CHIP_KICKER, EFeatureState.WORKING);
		result.put(EFeature.STRAIGHT_KICKER, EFeatureState.WORKING);
		result.put(EFeature.MOVE, EFeatureState.WORKING);
		result.put(EFeature.BARRIER, EFeatureState.WORKING);
		result.put(EFeature.CHARGE_CAPS, EFeatureState.WORKING);
		return result;
	}
	
	
	/**
	 * @param cmd
	 */
	public void execute(final ACommand cmd)
	{
	}
	
	
	/**
	 * This is called when the match command should be sent
	 */
	public void sendMatchCommand()
	{
	}
	
	
	/**
	 * Start bot
	 */
	public abstract void start();
	
	
	/**
	 * Stop bot
	 */
	public abstract void stop();
	
	
	@Override
	public double getKickerLevelMax()
	{
		return kickerLevelMax;
	}
	
	
	/**
	 * @param cmd
	 */
	public void onIncomingBotCommand(final ACommand cmd)
	{
		notifyIncommingBotCommand(cmd);
	}
	
	
	@Override
	public boolean isAvailableToAi()
	{
		return !isBlocked();
	}
	
	
	@Override
	public final EBotType getType()
	{
		return type;
	}
	
	
	@Override
	public String toString()
	{
		return "[Bot: " + type + "|" + getBotId() + "]";
	}
	
	
	@Override
	public final Map<EFeature, EFeatureState> getBotFeatures()
	{
		return botFeatures;
	}
	
	
	@Override
	public final String getControlledBy()
	{
		return controlledBy;
	}
	
	
	/**
	 * @param controlledBy the controlledBy to set
	 */
	public final void setControlledBy(final String controlledBy)
	{
		this.controlledBy = controlledBy;
	}
	
	
	@Override
	public final ETeamColor getColor()
	{
		return getBotId().getTeamColor();
	}
	
	
	@Override
	public final boolean isBlocked()
	{
		return !controlledBy.isEmpty();
	}
	
	
	@Override
	public final boolean isHideFromRcm()
	{
		return hideFromRcm;
	}
	
	
	/**
	 * @param hideFromRcm the hideFromRcm to set
	 */
	public final void setHideFromRcm(final boolean hideFromRcm)
	{
		this.hideFromRcm = hideFromRcm;
	}
	
	
	@Override
	public final BotID getBotId()
	{
		return botId;
	}
	
	
	/**
	 * @return the matchCtrl
	 */
	public final MatchCommand getMatchCtrl()
	{
		return matchCtrl;
	}
	
	
	/**
	 * @return the updateRate
	 */
	public final double getUpdateRate()
	{
		return updateRate;
	}
	
	
	/**
	 * @param updateRate the updateRate to set
	 */
	public final void setUpdateRate(final double updateRate)
	{
		this.updateRate = updateRate;
	}
	
	
	@Override
	public String getName()
	{
		return BOT_NAMES[getBotId().getNumber()];
	}
	
	
	/**
	 * @return the baseStation
	 */
	public final IBaseStation getBaseStation()
	{
		return baseStation;
	}
	
	
	@Override
	public Optional<IVector3> getSensoryPos()
	{
		return Optional.empty();
	}
	
	
	@Override
	public Optional<IVector3> getSensoryVel()
	{
		return Optional.empty();
	}
	
	
	@Override
	public synchronized Optional<TrajectoryWithTime<IVector3>> getCurrentTrajectory()
	{
		return Optional.ofNullable(curTrajectory);
	}
	
	
	/**
	 * @param curTrajectory
	 */
	public synchronized void setCurrentTrajectory(final TrajectoryWithTime<IVector3> curTrajectory)
	{
		this.curTrajectory = curTrajectory;
	}
	
	
	/** Common Bot Observer */
	@FunctionalInterface
	public interface IABotObserver
	{
		/**
		 * Called when a new command from the robot arrives.
		 * 
		 * @param cmd
		 */
		void onIncommingBotCommand(ACommand cmd);
	}
}