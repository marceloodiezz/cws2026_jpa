
package uo.ri.cws.domain;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import uo.ri.cws.domain.base.BaseEntity;

@Entity
@Table(name = "TRECOMMENDATIONS", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "RECOMMENDER_ID", "RECOMMENDED_ID" }) })
public class Recommendation extends BaseEntity {

	// Natural attributes
	private boolean usedForVoucher = false;

	// Accidental attributes
	@ManyToOne
	private Client recommender;

	@OneToOne
	private Client recommended;

	// ---- Constructors ----

	Recommendation() {
	}

	public Recommendation(Client recommender, Client recommended) {
		Associations.Recommends.link(recommender, this, recommended);
	}

	// ---- Getters ----

	public boolean isUsedForVoucher() {
		return usedForVoucher;
	}

	public Client getRecommender() {
		return recommender;
	}

	public Client getRecommended() {
		return recommended;
	}

	// ---- Association setters ----

	void _setRecommender(Client recommender) {
		this.recommender = recommender;
	}

	void _setRecommended(Client recommended) {
		this.recommended = recommended;
	}

	// ---- Domain methods ----

	public void markAsUsedForVoucher() {
		this.usedForVoucher = true;
	}

	// ---- Object methods ----

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Recommendation)) {
			return false;
		}

		Recommendation other = (Recommendation) obj;

		return Objects.equals(recommender, other.recommender) && Objects.equals(recommended, other.recommended);
	}

	@Override
	public int hashCode() {
		return Objects.hash(recommender, recommended);
	}

	@Override
	public String toString() {
		return "Recommendation [recommender=" + recommender.getNif() + ", recommended=" + recommended.getNif()
				+ ", usedForVoucher=" + usedForVoucher + "]";
	}
}
