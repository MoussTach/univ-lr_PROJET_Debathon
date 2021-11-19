INSERT INTO Tag (label) values 
    ('Famille'),
    ('Ecole'),
    ('Sexualité'),
    ('Logement'),
    ('Repas'),
    ('Permis'),
    ('Voyages'),
    ('Zemmour'),
    ('Le Pen'),
    ('Macron');

INSERT INTO Categorie (label) values 
    ('Politque'),
    ('Social'),
    ('Famille'),
    ('Transport'),
    ('Aides'),
    ('Etudes'),
    ('Voyage'),
    ('Echange');

INSERT INTO Utilisateur (label) VALUES
    ('Cheval bleu'),
    ('Lapin noir'),
    ('Chat rouge'),
    ('Aigle vert'),
    ('Vache violet'),
    ('Renard cyan'),
    ('Elephan rose');

INSERT INTO Salon (label, description, cle, mail, id_categorie) VALUES
    (
        'Éléction 2022',
        'L"éléction de 2022 arrive a grand pas !!! Alors débathon ;)',
        'HIOMLE',
        'mail@demo.fr',
        1
    );

INSERT INTO Relation_salon_tag (id_salon, id_tag) values 
    (1, 8),
    (1, 9),
    (1, 10);

INSERT INTO Relation_salon_utilisateur (id_salon, id_utilisateur) VALUES
    (1, 1),
    (1, 2),
    (1, 4),
    (1, 6);

INSERT INTO Question (label, contexte, id_salon) VALUES
    (
        'Pour combien de temps est élu un Président ?',
        'En France, le quinquennat désigne le mandat du président de la République française. La durée de ce mandat est définie dans l"article 6 de la Constitution de la Cinquième République française.',
        1
    ),
    (
        'Pour qui allez - vous voter ?',
        '',
        1
    ); 

INSERT INTO Commentaire (commentaire, nb_like, nb_dislike, nb_reaction, id_question, id_salon, id_utilisateur) VALUES
    (
        'Ouais ouais message contructif',
        0,
        0,
        0,
        1,
        1,
        1
    ),
    (
        'Tout à fait',
        0,
        0,
        0,
        1,
        1,
        2
    ),
    (
        'Je pense c"est 4ans',
        0,
        0,
        0,
        1,
        1,
        6
    ),
    (
        'VIVE ZEMMOURRRR !!!!',
        1,
        3,
        0,
        1,
        1,
        1
    );

INSERT INTO QCM (label, nb_vote, id_question, id_salon) VALUES
    ('4 ans', 1, 1, 1),
    ('5 ans', 3, 1, 1),
    ('7 ans', 1, 1, 1),
    ('Marine Le Pen', 2, 2, 1),
    ('Érice Zemmour', 1, 2, 1),
    ('Emmanuel Macron', 1, 2, 1);
    