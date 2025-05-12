import { useState, type ChangeEvent } from "react";
import { postAdd } from "../../api/todoApi";
import ResultModal from "../common/resultModal";
import useCustomMove from "../../hooks/useCustomMove";

const initState: TodoAdd = {
  title: "",
  writer: "",
  dueDate: null,
};

function AddComponent() {
  const [todo, setTodo] = useState<TodoAdd>({ ...initState });
  const [result, setResult] = useState<number | null>(null);
  const { moveToList } = useCustomMove();

  const handleChangeTodo = (e: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setTodo((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleClickAdd = (): void => {
    postAdd(todo)
      .then((result) => {
        console.log(result);
        setResult(result.tno);
        // 초기화
        setTodo({ ...initState });
      })
      .catch((error) => {
        console.error("Error adding todo:", error);
      });
  };

  const closeModal = () => {
    setResult(null);
    moveToList(); // 모달 닫기 후 목록으로 이동
  };

  return (
    <div className="border-2 border-sky-200 mt-10 m-2 p-4">
      {result && (
        <ResultModal
          title="등록 처리 완료"
          content={`${result}번 글이 등록되었습니다.`}
          callbackFn={closeModal} // 모달 닫기 함수
        />
      )}

      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">TITLE</div>
          <input
            className="w-4/5 p-6 rounded-r border border-solid border-neutral-500 shadow-md"
            name="title"
            type={"text"}
            value={todo.title}
            onChange={handleChangeTodo}
          ></input>{" "}
        </div>
      </div>
      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">WRITER</div>
          <input
            className="w-4/5 p-6 rounded-r border border-solid border-neutral-500 shadow-md"
            name="writer"
            type={"text"}
            value={todo.writer}
            onChange={handleChangeTodo}
          ></input>
        </div>
      </div>

      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">DUEDATE</div>
          <input
            className="w-4/5 p-6 rounded-r border border-solid border-neutral-500 shadow-md"
            name="dueDate"
            type={"date"}
            value={todo.dueDate ? todo.dueDate.split("T")[0] : ""}
            // value={todo.dueDate}
            onChange={handleChangeTodo}
          ></input>
        </div>
      </div>
      <div className="flex justify-end">
        <div className="relative mb-4 flex p-4 flex-wrap items-stretch">
          <button
            type="button"
            className="rounded p-4 w-36 bg-blue-500 text-xl text-white "
            onClick={handleClickAdd}
          >
            ADD
          </button>
        </div>
      </div>
    </div>
  );
}

export default AddComponent;
