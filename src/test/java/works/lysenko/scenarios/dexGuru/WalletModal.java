package works.lysenko.scenarios.dexGuru;

import static works.lysenko.C.PROVIDER_ITEM;
import static works.lysenko.C.PROVIDER_MENU;

import java.util.Set;

import works.lysenko.Execution;
import works.lysenko.scenarios.AbstractNodeScenario;
import works.lysenko.scenarios.dexGuru.walletModal.Buy;
import works.lysenko.scenarios.dexGuru.walletModal.Sell;
import works.lysenko.scenarios.dexGuru.walletModal.Wallet;

@SuppressWarnings("javadoc")
public class WalletModal extends AbstractNodeScenario {

	public WalletModal(Execution x) {
		super(Set.of(new Buy(x), new Sell(x), new Wallet(x)), x);
	}

	@Override
	public void action() {
		section("Verifying wallet modal window");
	}

	@Override
	public void finals() {
		section("Verifying wallets");
		wait(PROVIDER_MENU);
		wait(fill(PROVIDER_ITEM, "MetaMask"));
		wait(fill(PROVIDER_ITEM, "WalletConnect"));
		wait(fill(PROVIDER_ITEM, "Binance Wallet"));
	}

	@Override
	public boolean sufficed() {
		return true;
	}
}
