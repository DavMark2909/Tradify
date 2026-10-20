import { useState } from 'react';
import './styles/ItemForm.css';

const EMPTY_ITEM = {
  title: '',
  description: '',
  price: '',
  currency: 'USD',
  unitOfMeasure: '',
  status: 'Available',
  availableQuantity: '',
};

const CURRENCIES = ['USD', 'EUR', 'GBP'];
const STATUSES = ['Available', 'Unavailable', 'Closed'];

function validate(values) {
  const errors = {};
  if (!values.title.trim()) errors.title = 'Title is required.';
  const price = Number(values.price);
  if (values.price === '' || Number.isNaN(price) || price <= 0) {
    errors.price = 'Enter a price greater than 0.';
  }
  const quantity = Number(values.availableQuantity);
  if (values.availableQuantity === '' || !Number.isInteger(quantity) || quantity < 0) {
    errors.availableQuantity ='Enter a whole number of 0 or more.';
  }
  return errors;
}

export default function ItemForm({ initialValues, onSubmit, onCancel, submitLabel = 'Save item' }) {
  const [values, setValues] = useState({ ...EMPTY_ITEM, ...initialValues });
  const [errors, setErrors] = useState({});
  const [submitError, setSubmitError] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setValues((prev) => ({ ...prev, [name]: value }));
    if (errors[name]) setErrors((prev) => ({ ...prev, [name]: undefined }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const nextErrors = validate(values);
    setErrors(nextErrors);
    if (Object.keys(nextErrors).length > 0) return;

    setSubmitting(true);
    setSubmitError('');
    try {
      await onSubmit({
        ...values,
        title: values.title.trim(),
        description: values.description.trim(),
        unitOfMeasure: values.unitOfMeasure.trim(),
        price: Number(values.price),
        availableQuantity: Number(values.availableQuantity),
      });
    } catch {
      setSubmitError("Couldn't save the item. Please try again.");
      setSubmitting(false);
    }
  };

  return (
    <form className="item-form" onSubmit={handleSubmit} noValidate>
      <label className="item-form__field">
        <span>Title</span>
        <input
          name="title"
          value={values.title}
          onChange={handleChange}
          aria-invalid={!!errors.title}
          autoFocus
        />
        {errors.title && <small className="item-form__error">{errors.title}</small>}
      </label>

      <label className="item-form__field">
        <span>Description</span>
        <textarea name="description" rows={4} value={values.description} onChange={handleChange} />
      </label>

      <div className="item-form__row">
        <label className="item-form__field">
          <span>Price</span>
          <input
            name="price"
            type="number"
            min="0"
            step="0.01"
            inputMode="decimal"
            value={values.price}
            onChange={handleChange}
            aria-invalid={!!errors.price}
          />
          {errors.price && <small className="item-form__error">{errors.price}</small>}
        </label>

        <label className="item-form__field">
          <span>Currency</span>
          <select name="currency" value={values.currency} onChange={handleChange}>
            {CURRENCIES.map((c) => (
              <option key={c} value={c}>{c}</option>
            ))}
          </select>
        </label>

        <label className="item-form__field">
          <span>Measure</span>
          <input name="unitOfMeasure" placeholder="e.g. kg, unit, m²" value={values.unitOfMeasure} onChange={handleChange} />
        </label>
      </div>

      <div className="item-form__row item-form__row--2">
        <label className="item-form__field">
          <span>Status</span>
          <select name="status" value={values.status} onChange={handleChange}>
            {STATUSES.map((s) => (
              <option key={s} value={s}>{s}</option>
            ))}
          </select>
        </label>

        <label className="item-form__field">
          <span>Available quantity</span>
          <input
            name="availableQuantity"
            type="number"
            min="0"
            step="1"
            inputMode="numeric"
            value={values.availableQuantity}
            onChange={handleChange}
            aria-invalid={!!errors.availableQuantity}
          />
          {errors.availableQuantity && (
            <small className="item-form__error">{errors.availableQuantity}</small>
          )}
        </label>
      </div>

      {submitError && <p className="item-form__error item-form__error--submit">{submitError}</p>}

      <div className="item-form__actions">
        <button type="button" className="item-form__btn" onClick={onCancel} disabled={submitting}>
          Cancel
        </button>
        <button type="submit" className="item-form__btn item-form__btn--primary" disabled={submitting}>
          {submitting ? 'Saving…' : submitLabel}
        </button>
      </div>
    </form>
  );
}
