package loqor.ait.client.registry.exterior.impl.classic;

import org.joml.Vector3f;

public class ClientClassicBoxDefinitiveVariant extends ClientClassicBoxVariant {
	public ClientClassicBoxDefinitiveVariant() {
		super("definitive");
	}

	@Override
	public Vector3f sonicItemTranslations() {
		return new Vector3f(0.55f, 1.125f, 1.165f);
	}
}
