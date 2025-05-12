interface Todo {
  tno: number;
  title: string;
  writer: string;
  dueDate: string | null;
  completed: boolean;
}

interface TodoAdd {
  title: string;
  writer: string;
  dueDate: string | null;
}

interface TodoModify {
  tno: number;
  title: string;
  dueDate: string | null;
  completed: boolean;
}
