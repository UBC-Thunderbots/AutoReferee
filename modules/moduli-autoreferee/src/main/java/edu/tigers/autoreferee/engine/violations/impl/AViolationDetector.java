/*
 * *********************************************************
 * Copyright (c) 2009 - 2016, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: Feb 7, 2016
 * Author(s): "Lukas Magel"
 * *********************************************************
 */
package edu.tigers.autoreferee.engine.violations.impl;

import java.util.Arrays;
import java.util.List;

import com.github.g3force.configurable.ConfigRegistration;

import edu.tigers.autoreferee.engine.violations.IViolationDetector;
import edu.tigers.sumatra.wp.data.EGameStateNeutral;


/**
 * Abstract base class that contains common operations for the game rules
 * 
 * @author "Lukas Magel"
 */
public abstract class AViolationDetector implements IViolationDetector
{
	private final List<EGameStateNeutral>	activeStates;
	
	
	/**
	 * @param gamestate The gamestate this rule will be active in
	 */
	public AViolationDetector(final EGameStateNeutral gamestate)
	{
		this(Arrays.asList(gamestate));
	}
	
	
	/**
	 * @param activeStates the list of game states that the rule will be active in
	 */
	public AViolationDetector(final List<EGameStateNeutral> activeStates)
	{
		this.activeStates = activeStates;
	}
	
	
	@Override
	public boolean isActiveIn(final EGameStateNeutral state)
	{
		return activeStates.contains(state);
	}
	
	
	protected static void registerClass(final Class<?> clazz)
	{
		ConfigRegistration.registerClass("autoreferee", clazz);
	}
}