package loqor.ait.tardis.exterior.variant.classic.client;

import org.joml.Vector3f;

public class ClientClassicBoxMintVariant extends ClientClassicBoxVariant {

	public ClientClassicBoxMintVariant() {
		super("mint");
	}

	@Override
	public Vector3f sonicItemTranslations() {
		return new Vector3f(0.55f, 1.125f, 1.165f);
	}
}
