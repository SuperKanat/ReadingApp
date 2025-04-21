CREATE TABLE Files (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    file_path TEXT NOT NULL UNIQUE,
    size INTEGER NOT NULL,
    format TEXT NOT NULL CHECK(format IN ('fb2', 'epub', 'pdf')),
    author TEXT,
    preview_path TEXT,
    added_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status INTEGER,
    page_count INTEGER,
    current_page INTEGER DEFAULT 0 CHECK(current_page <= page_count),
    last_opened_at DATETIME
);

CREATE TABLE Groups (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL UNIQUE,
    color_code TEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE GroupFiles (
    group_id INTEGER,
    file_id INTEGER,
    added_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (group_id, file_id),
    FOREIGN KEY (group_id) REFERENCES Groups(id) ON DELETE CASCADE,
    FOREIGN KEY (file_id) REFERENCES Files(id) ON DELETE CASCADE
);
