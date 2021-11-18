CREATE TABLE IF NOT EXISTS Tag (
    idTag integer primary key autoincrement not null,

    label varchar(20) not null
);


CREATE TABLE IF NOT EXISTS Categorie (
    idCategorie integer primary key autoincrement not null,
    
    label varchar(20) not null
);


CREATE TABLE IF NOT EXISTS Utilisateur (
    idUtilisateur integer primary key autoincrement not null,
    
    label varchar(20) not null
);


CREATE TABLE IF NOT EXISTS Salon (
    idSalon integer primary key autoincrement not null,
    
    label varchar(50) not null,
    description TEXT,
    cle varchar(6) not null,
    mail varchar(100) not null,
    
    est_ouvert boolean DEFAULT true,
    
    date_debut DATE DEFAULT (datetime('now','localtime')),
    date_fin DATE,
    
    id_categorie integer,
    
    FOREIGN KEY (id_categorie) REFERENCES Categorie (idCategorie)
);


CREATE TABLE IF NOT EXISTS Relation_salon_tag (
    idRelation integer primary key autoincrement not null,
    
    id_salon integer,
    id_tag integer,
    
    FOREIGN KEY (id_salon) REFERENCES Salon (idSalon),
    FOREIGN KEY (id_tag) REFERENCES Tag (idTag)
);


CREATE TABLE IF NOT EXISTS Relation_salon_utilisateur (
    idRelation integer primary key autoincrement not null,
    
    id_salon integer,
    id_utilisateur integer,
    
    FOREIGN KEY (id_salon) REFERENCES Salon (idSalon),
    FOREIGN KEY (id_utilisateur) REFERENCES Utilisateur (idUtilisateur)
);


CREATE TABLE IF NOT EXISTS Question (
    idQuestion integer primary key autoincrement not null,
    
    label varchar(150) not null,
    contexte TEXT,
    
    est_active boolean DEFAULT true,
    
    id_salon integer,
    
    FOREIGN KEY (id_salon) REFERENCES Salon (idSalon)
);


CREATE TABLE IF NOT EXISTS Commentaire (
    idCommentaire integer primary key autoincrement not null,
    
    commentaire varchar(50) not null,
    
    nb_like integer DEFAULT 0,
    nb_dislike integer DEFAULT 0,
    nb_reaction integer DEFAULT 0,
    
    id_parent integer,
    id_question integer,
    id_salon integer,
    id_utilisateur integer,
    
    FOREIGN KEY (id_parent) REFERENCES Commentaire (idCommentaire),
    FOREIGN KEY (id_question) REFERENCES Question (idQuestion),
    FOREIGN KEY (id_salon) REFERENCES Salon (idSalon),
    FOREIGN KEY (id_utilisateur) REFERENCES Utilisateur (idUtilisateur)
);


CREATE TABLE IF NOT EXISTS QCM (
    idQCM integer primary key autoincrement not null,
    
    label varchar(50) not null,
    
    nb_vote integer DEFAULT 0,

    id_question integer,
    id_salon integer,
    
    FOREIGN KEY (id_question) REFERENCES Question (idQuestion),
    FOREIGN KEY (id_salon) REFERENCES Salon (idSalon)
);