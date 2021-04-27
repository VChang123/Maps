function TextBox(props) {
    const change = (event) => {
        props.change(event.target.value)
    }
    return(
        <div className={"TextBox"}>
            <label>
                {props.label}
                <input type = "Text" onChange = {change} value = {props.value === 0 ? " ": props.value}></input>
            </label>
        </div>
    );

}
export default TextBox;