/*
 * generated by Xtext 2.22.0
 */
package fr.gwendal_jouneaux.rob_lang;


/**
 * Initialization support for running Xtext languages without Equinox extension registry.
 */
public class RobLANGStandaloneSetup extends RobLANGStandaloneSetupGenerated {

	public static void doSetup() {
		new RobLANGStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}
