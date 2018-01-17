/*
 * Copyright (c) 2009 - 2017, DHBW Mannheim - TIGERs Mannheim
 */

package edu.tigers.sumatra.vision;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.log4j.Logger;

import edu.tigers.moduli.AModule;
import edu.tigers.moduli.exceptions.InitModuleException;
import edu.tigers.moduli.exceptions.ModuleNotFoundException;
import edu.tigers.moduli.exceptions.StartModuleException;
import edu.tigers.sumatra.bot.RobotInfo;
import edu.tigers.sumatra.cam.ACam;
import edu.tigers.sumatra.cam.ICamFrameObserver;
import edu.tigers.sumatra.cam.data.CamDetectionFrame;
import edu.tigers.sumatra.ids.BotID;
import edu.tigers.sumatra.math.vector.IVector3;
import edu.tigers.sumatra.model.SumatraModel;
import edu.tigers.sumatra.vision.data.FilteredVisionFrame;


/**
 * Module for processing raw vision data.
 *
 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
 */
public abstract class AVisionFilter extends AModule implements ICamFrameObserver
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(AVisionFilter.class.getName());
	
	private Map<BotID, RobotInfo> robotInfoMap = new HashMap<>();
	private final Set<IVisionFilterObserver> observers = new CopyOnWriteArraySet<>();
	
	
	public Map<BotID, RobotInfo> getRobotInfoMap()
	{
		return robotInfoMap;
	}
	
	
	public void setRobotInfoMap(final Map<BotID, RobotInfo> robotInfoMap)
	{
		this.robotInfoMap = robotInfoMap;
	}
	
	
	/**
	 * Update filter with a new camera detection frame from one camera
	 *
	 * @param camDetectionFrame detections from a single camera frame
	 */
	protected abstract void updateCamDetectionFrame(CamDetectionFrame camDetectionFrame);
	
	/**
	 * Called once uppon moduli start
	 */
	protected void start()
	{
		// nothing to do
	}
	
	
	/**
	 * Called once uppon moduli stop
	 */
	protected void stop()
	{
		robotInfoMap = new HashMap<>();
	}
	
	
	/**
	 * Send a complete and filtered vision frame to external modules
	 *
	 * @param filteredVisionFrame the filtered and complete vision frame
	 */
	protected final void publishFilteredVisionFrame(final FilteredVisionFrame filteredVisionFrame)
	{
		observers.forEach(o -> o.onNewFilteredVisionFrame(filteredVisionFrame));
	}
	
	
	/**
	 * Reset the ball to a new position. Can be used to:<br>
	 * - select another ball, if multiple balls are detected (real filter)
	 * - actively put the ball somewhere else (simulator)
	 * 
	 * @param pos where the ball should be reset to
	 * @param vel
	 */
	public void resetBall(final IVector3 pos, final IVector3 vel)
	{
	}
	
	
	/**
	 * Register for filtered vision frames
	 *
	 * @param observer to register to the vision filter
	 */
	public final void addObserver(final IVisionFilterObserver observer)
	{
		observers.add(observer);
	}
	
	
	/**
	 * Unregister for filtered vision frames
	 *
	 * @param observer to unregister
	 */
	public final void removeObserver(final IVisionFilterObserver observer)
	{
		observers.remove(observer);
	}
	
	
	@Override
	public void initModule() throws InitModuleException
	{
		// nothing to do
	}
	
	
	@Override
	public void deinitModule()
	{
		// nothing to do
	}
	
	
	@Override
	public void startModule() throws StartModuleException
	{
		start();
		try
		{
			ACam cam = SumatraModel.getInstance().getModule(ACam.class);
			cam.addObserver(this);
		} catch (ModuleNotFoundException e)
		{
			log.error("Could not find cam module.", e);
		}
	}
	
	
	@Override
	public void stopModule()
	{
		try
		{
			ACam cam = SumatraModel.getInstance().getModule(ACam.class);
			cam.removeObserver(this);
		} catch (ModuleNotFoundException e)
		{
			log.error("Could not find cam module.", e);
		}
		stop();
	}
	
	
	@Override
	public final void onNewCamDetectionFrame(final CamDetectionFrame camDetectionFrame)
	{
		try
		{
			updateCamDetectionFrame(camDetectionFrame);
		} catch (Throwable e)
		{
			log.error("Error during cam detection processing", e);
		}
	}
	
	
	@Override
	public void onClearCamFrame()
	{
		robotInfoMap = new HashMap<>();
	}
}