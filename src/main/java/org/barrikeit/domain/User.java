package org.barrikeit.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
@AttributeOverride(name = "id", column = @Column(name = "id_user", nullable = false))
public class User extends GenericEntity {
  @Serial private static final long serialVersionUID = 1L;

  private String username;
  private String email;

  @Override
  public String toString() {
    return "User{" + "id=" + id + '}';
  }
}
