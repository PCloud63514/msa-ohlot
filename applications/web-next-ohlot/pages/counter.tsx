import {useState} from "react";

export default function CounterPage() {
     const [count, setCount] = useState(0)
    return (
        <div>
            <h1> Counter : {count}</h1>
            <button onClick={()=> {setCount(prev => prev + 1)}}>click!</button>
        </div>
    )
}