package org.examplee.proyecto_intento.entity;

public interface InvasionParticipant {
    CombatState combat();
    int combatRole();
    void combatRole(int role);
}
